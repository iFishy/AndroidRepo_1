package com.clcoulte.gsort.MailUtil;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;

import android.os.AsyncTask;
import android.util.Log;

import com.clcoulte.gsort.Util.Address;
import com.clcoulte.gsort.Util.ProgressTracker;
import com.clcoulte.gsort.Util.TreeNode;

/*
 * GMailRunner
 * 
 * Accesses GMail login and retrieve basic info on email using IMAP 
 */
public class GMailRunner {

	public Store store = null;

	private static final String TAG = "GMailRunner";

	public static final String DEFAULT_LOGIN_UN = "clcoulte@gmail.com",
			DEFAULT_LOGIN_PW = "pzrsekoeyorxzlaz";

	private static final String DEF_FOLDER = "All Mail";

	private ProgressTracker progressTracker;

	public GMailRunner() {
		new GMailRunner(DEFAULT_LOGIN_UN, DEFAULT_LOGIN_PW, null);
	}

	public GMailRunner(ProgressTracker progressTracker) {
		new GMailRunner(DEFAULT_LOGIN_UN, DEFAULT_LOGIN_PW, progressTracker);
	}

	public GMailRunner(final String un, final String pw,
			ProgressTracker progressTracker) {

		this.progressTracker = progressTracker;

		Properties props = new Properties();

		// props.setProperty("mail.debug", boolean);
		// props.setProperty("mail.from", String);
		// props.setProperty("mail.mime.address.strict", String);
		props.setProperty("mail.host", "imap.gmail.com");
		props.setProperty("mail.store.protocol", "imaps");
		props.setProperty("mail.transport.protocol", "smtp");
		// props.setProperty("mail.user", un);
		// props.setProperty("mail.imap.class", String);
		props.setProperty("mail.imap.host", "imap.gmail.com");
		// props.setProperty("mail.imap.port", int);
		props.setProperty("mail.imap.user", un);

		Session session = Session.getInstance(props, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(un, pw);
			}
		});

		try {
			store = session.getStore();
		} catch (NoSuchProviderException e) {
			Log.v(TAG, e.getMessage());
		}

		new ConnectTask().execute(store);

	}

	private class ConnectTask extends AsyncTask<Store, Void, Exception> {

		// Class currently only takes one parameter**

		protected void onPreExecute() {
		}

		protected Exception doInBackground(Store... stores) {
			try {
				stores[0].connect();
				// Log.v(TAG, "isConnected() - " + stores[0].isConnected()
				// + " to " + stores[0].getURLName().toString());
				return null;
			} catch (Exception e) {
				// MessagingException
				Log.v(TAG, e.getMessage());
				return e;
			}
		}

		protected void onProgressUpdate() {

		}

		protected void onPostExecute(Exception e) {
			if (e == null) {
				Log.v(TAG, "CONNECTED!");
			}
		}

	}

	public TreeNode getAllMailTree() {
		TreeNode root = new TreeNode<String>();

		if (!store.isConnected()) {
			Log.v(TAG, "getAllMailTree() - store not connected");
			return null;
		}

		Folder f;
		try {
			f = store.getFolder(DEF_FOLDER);
		} catch (MessagingException e1) {
			Log.v(TAG,
					"Can't find folder - " + DEF_FOLDER + " - "
							+ e1.getMessage());
			return null;
		}

		try {
			f.open(Folder.READ_ONLY);
		} catch (MessagingException e) {
			Log.v(TAG,
					"Can't open folder - " + f.getFullName() + " - "
							+ e.getMessage());
			return null;
		}
		
		

		return root;
	}

	public ProgressTracker getProgressTracker() {
		return progressTracker;
	}

	public void setProgressTracker(ProgressTracker progressTracker) {
		this.progressTracker = progressTracker;
	}
}
