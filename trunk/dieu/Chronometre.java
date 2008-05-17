package dieu;

public class Chronometre{
	
	Thread thread;
	boolean stop;
	int sec, min;
	public Chronometre(){
		stop = false;
	}
	
	public void initialiser(int sec, int min){
		
	}
	private class Comptage implements Runnable{

		public void run(){
			try{
					while(! stop){
						Thread.sleep(1000);
						sec=sec-1;
						if(sec == -1){
							sec=59;
							min=min-1;
						}
					}
			}
			catch (InterruptedException e) {
			}
			
		}
			
			
		
		
	}
	
	public void demarrer(){
		if(thread == null){
			Thread compteur = new Thread(new Comptage());
		}
		thread.start();
	}
	
	public void stopper(){
		stop = true;
	}
}
