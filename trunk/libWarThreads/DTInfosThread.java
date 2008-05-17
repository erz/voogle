package libWarThreads;

@SuppressWarnings("serial")
public class DTInfosThread extends DonneeTrame {
	
	private InfoThread info;
	
	public DTInfosThread(InfoThread infoThread) {
		info = infoThread;
	}
	
	public InfoThread getInfoThread() {
		return info;
	}

}
