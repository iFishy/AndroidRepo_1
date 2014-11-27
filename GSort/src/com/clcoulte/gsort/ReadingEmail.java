import java.util.*;
import javax.mail.internet.InternetAddress;
import javax.mail.*;

public class ReadingEmail
{

	public static class SortOrderKey
	{
		public static final int DOMAIN = 1, LOCAL = 2, COUNT = 3;
		public static final int[] DEFAULT = new int[]
		{ DOMAIN, LOCAL, COUNT };
	}

	public static ArrayList<AddressHistory> getAddressAnalysis(
			String folderName, String username, String pw,
			final int[] sortingOrder)
	{

		Properties props = new Properties();
		props.setProperty("mail.store.protocol", "imaps");
		try
		{
			Session session = Session.getInstance(props, null);
			// session.setDebug(true);
			Store store = session.getStore();
			store.connect("imap.gmail.com", username, pw);

			ArrayList<AddressHistory> a = recursiveGetAddresses(store
					.getFolder(folderName));

			Collections.sort(a, new Comparator<AddressHistory>()
			{

				@Override
				public int compare(AddressHistory arg0, AddressHistory arg1)
				{
					int c = 0;
					for (int i = 0; i < sortingOrder.length && c == 0; i++)
					{

						switch (sortingOrder[i])
						{
						case SortOrderKey.DOMAIN:
							String[] ds0 = arg0.getReversedDomainArray();
							String[] ds1 = arg1.getReversedDomainArray();
							String r0 = "";
							String r1 = "";
							for (int j = 0; j < ds0.length; j++)
							{
								r0 += ds0[j] + ".";
							}
							for (int k = 0; k < ds1.length; k++)
							{
								r1 += ds1[k] + ".";
							}
							c = r0.compareTo(r1);
						case SortOrderKey.LOCAL:
							c = arg0.a.getAddress().split("@")[0]
									.compareTo(arg1.a.getAddress().split("@")[0]);
						case SortOrderKey.COUNT:
							c = arg0.count - arg1.count;
						}
					}
					return c;
				}
			});

			return a;

		} catch (Exception mex)
		{
			mex.printStackTrace();
			return null;
		}

	}

	public static void main(String[] args)
	{
		Properties props = new Properties();
		props.setProperty("mail.store.protocol", "imaps");
		try
		{
			Session session = Session.getInstance(props, null);
			// session.setDebug(true);
			Store store = session.getStore();
			store.connect("imap.gmail.com", "clcoulte@gmail.com",
					"cgsbqpwjkdsyjhmv");

			Folder root = store.getDefaultFolder();
			root = root.getFolder("[Gmail]/All Mail");
			// root = root.getFolder("Kelli");
			// root = root.getFolder("Expenses");
			ArrayList<AddressHistory> a = recursiveGetAddresses(root);

			Collections.sort(a, new Comparator<AddressHistory>()
			{

				@Override
				public int compare(AddressHistory arg0, AddressHistory arg1)
				{

					String[] ds0 = arg0.a.getAddress().split("@")[1]
							.split("\\.");
					String[] ds1 = arg1.a.getAddress().split("@")[1]
							.split("\\.");

					Collections.reverse(Arrays.asList(ds0));
					Collections.reverse(Arrays.asList(ds1));
					String r0 = "";
					String r1 = "";
					for (int i = 0; i < ds0.length; i++)
					{
						r0 += ds0[i] + ".";
					}
					for (int i = 0; i < ds1.length; i++)
					{
						r1 += ds1[i] + ".";
					}

					// System.out.println(r0 + " - " + r1);

					int c = r0.compareTo(r1);
					if (c == 0)
					{
						int diff = arg1.count - arg0.count;
						if (diff != 0)
						{
							return diff;
						} else
						{
							return arg0.a.getAddress().split("@")[0]
									.compareTo(arg1.a.getAddress().split("@")[0]);
						}

					}

					return c;

				}

			});

			// sort based on count, then alphabet
			// Collections.sort(a, new Comparator<AddressHistory>() {
			//
			// @Override
			// public int compare(AddressHistory arg0, AddressHistory arg1) {
			// return arg1.count - arg0.count;
			// }
			//
			// });

			for (int i = 0; i < a.size(); i++)
			{
				System.out.println("" + a.get(i).count + " - "
						+ a.get(i).a.getAddress());
			}

			// Folder[] folders = root.list();
			// System.out.println("Number of Folders = " + folders.length);

			// root.open(Folder.READ_ONLY);

			// for (int i = 0; i < folders.length; i++) {
			//
			// System.out.println(" - " + folders[i].getName());
			// }
			// Message[] messages = recursiveGetMessages(root);

			// root = root.getFolder("Kelli");
			// root.open(Folder.READ_ONLY);
			// root.close(false);
			// Flags seen = new Flags(Flags.Flag.USER);
			// FlagTerm unseenFlagTerm = new FlagTerm(seen, false);
			// Message[] messages = root.search(unseenFlagTerm);
			//
			// System.out.println("" + messages.length);

			/*
			 * Folder inbox = store.getFolder("ALL_MAIL");
			 * inbox.open(Folder.READ_ONLY); Message msg =
			 * inbox.getMessage(inbox.getMessageCount()); Address[] in =
			 * msg.getFrom(); for (Address address : in) {
			 * System.out.println("FROM:" + address.toString()); } Multipart mp
			 * = (Multipart) msg.getContent(); BodyPart bp = mp.getBodyPart(0);
			 * System.out.println("SENT DATE:" + msg.getSentDate());
			 * System.out.println("SUBJECT:" + msg.getSubject());
			 * System.out.println("CONTENT:" + bp.getContent());
			 */
		} catch (Exception mex)
		{
			mex.printStackTrace();
		}
	}

	public static class AddressHistory
	{
		public InternetAddress a;
		public int count = 1;

		public AddressHistory(InternetAddress a)
		{
			this.a = a;
		}

		public boolean equals(Object o)
		{
			if (((InternetAddress) ((AddressHistory) o).a).getAddress().equals(
					((InternetAddress) a).getAddress()))
			{
				// System.out.println("Equal"
				// + ((InternetAddress) ((AddressHistory) o).a)
				// .getAddress());
				return true;
			}
			// System.out.println("Not Equal");
			return false;
		}

		public String[] getReversedDomainArray()
		{
			String[] ds0 = a.getAddress().split("@")[1].split("\\.");
			Collections.reverse(Arrays.asList(ds0));
			
			for (int i = 2; i < ds0.length - 1; i++)
			{
				ds0[i] += ds0[i - 1];
			}
			return ds0;
		}

		public String getLocal()
		{
			return a.getAddress().split("@")[0];
		}
	}

	private static void addToHistogram(ArrayList<AddressHistory> histogram,
			InternetAddress a)
	{
		AddressHistory ah = new AddressHistory(a);
		int index = histogram.indexOf(ah);
		if (index >= 0)
		{
			histogram.get(index).count++;
		} else
		{
			// doesn't exist, so add it
			histogram.add(ah);
		}
	}

	/*
	 * 
	 * *Address analysis has to be done on the fly because once the folders are
	 * closed, you can't access the addresses
	 */
	private static ArrayList<AddressHistory> recursiveGetAddresses(Folder f)
			throws MessagingException
	{
		// open folder, if you can, if not, then there can't be any messages
		// here
		try
		{
			f.open(Folder.READ_ONLY);
		} catch (MessagingException e)
		{
			System.out.println("Can't open folder - " + f.getFullName() + " - "
					+ e.getMessage());
		}

		Folder[] subFolders = f.list();
		ArrayList<AddressHistory> histogram = new ArrayList<AddressHistory>();
		Message[] messages;
		if (f.isOpen())
		{
			// get and analyze messages of current folder
			// the "magic" - where all the shit happens
			messages = f.getMessages();
			System.out.println("Analyzing " + messages.length
					+ " Messages from - " + f);
			for (int i = 0; i < messages.length; i++)
			{
				InternetAddress[] addresses = (InternetAddress[]) messages[i]
						.getFrom();
				System.out.print("" + addresses.length + ", ");
				if ((i + 1) % 20 == 0)
				{
					System.out.println();
					System.out.println("%" + (100 * i / messages.length));
				}
				if (addresses != null)
				{
					for (int k = 0; k < addresses.length; k++)
					{
						addToHistogram(histogram, addresses[k]);
					}
				} else
				{
					// From field does not exist in this message?
				}

			}

			f.close(false);
		}
		// iterate through sub-folders and add all addresses to the histogram
		for (int i = 0; i < subFolders.length; i++)
		{
			concatHistograms(histogram, recursiveGetAddresses(subFolders[i]));
		}
		System.out.println();
		return histogram;

	}

	/*
	 * Merges two histograms 'one' and 'two' into 'one', leaving 'two' alone.
	 * *Can be sped up a little by removing items from 'two' that are duplicated
	 * in 'one' and then just adding remaining items at end, but this affects
	 * the data in 'two'
	 */
	private static void concatHistograms(ArrayList<AddressHistory> one,
			ArrayList<AddressHistory> two)
	{
		for (int i = 0; i < one.size(); i++)
		{
			int index = (two.indexOf(one.get(i)));
			if (index >= 0)
			{
				// if 'two' contains the address of the current index of 'one'
				// then add the count to the index at 'one'
				// leave 'two' alone
				one.get(i).count += two.get(index).count;
			} // otherwise it is an address only in 'one'
		}
		for (int i = 0; i < two.size(); i++)
		{
			// iterate through 'two' to get and add remaining non-duplicates
			if (!one.contains(two.get(i)))
			{
				// if 'one' doesn't contain current 'i'tem in 'two'
				// then add it to one
				one.add(two.get(i));
			}
		}
		// 'one' should be the concatenated histogram now
	}

	private static Message[] recursiveGetMessages(Folder f)
			throws MessagingException
	{
		// open folder, if you can, if not, then there can't be any messages
		// here
		try
		{
			f.open(Folder.READ_ONLY);
		} catch (MessagingException e)
		{
			System.out.println("Can't open folder - " + f.getFullName());
			System.out.println("" + e.getMessage());
		}

		Folder[] subFolders = f.list();
		Message[] messages;
		if (subFolders.length == 0)
		{
			// we're at the end, turn back
			messages = f.getMessages();
			f.close(false);
			return messages;
		} else
		{

			if (f.isOpen())
			{
				messages = f.getMessages();
				f.close(false);
			} else
			{
				// if it isn't open, then you can't read from this folder
				messages = new Message[]
				{};
			}

			for (int i = 0; i < subFolders.length; i++)
			{
				System.out.println(" - " + subFolders[i].getFullName());
				messages = concatArrays(messages,
						recursiveGetMessages(subFolders[i]));
			}
		}
		return messages;

	}

	private static Message[] concatArrays(Message[] one, Message[] two)
	{
		if (one.length == 0)
		{
			return two;
		} else if (two.length == 0)
		{
			return one;
		} else
		{
			Message[] added = new Message[one.length + two.length];
			System.arraycopy(one, 0, added, 0, one.length);
			System.arraycopy(two, 0, added, one.length, two.length);
			return added;
		}
	}
}