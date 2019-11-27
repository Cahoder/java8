package GUI;

public class Main{
	public static void main(String[] args) {
		RegisterFrame GUI = new RegisterFrame();
//		SearchFrame GUI = SearchFrame.getInstance();
		SQLEvent.SetClient(GUI);
	}
}
