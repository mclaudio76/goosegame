package goosegame;

public class Player {
	
	private String ID 			= "";
	private int    gamePosition = 0;
	
	public Player(String ID) {
		this.ID = ID;
	}
	
	
	public String getID() {
		return ID;
	}


	public int getGamePosition() {
		return gamePosition;
	}


	public void setGamePosition(int gamePosition) {
		this.gamePosition = gamePosition;
	}


	public String toString() {
		return this.ID;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ID == null) ? 0 : ID.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		if (ID == null) {
			if (other.ID != null)
				return false;
		} else if (!ID.equals(other.ID))
			return false;
		return true;
	}
	
	
	
}
