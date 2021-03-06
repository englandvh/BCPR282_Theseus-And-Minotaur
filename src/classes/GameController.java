package classes;

import enums.Direction;
import enums.Wall;
import interfaces.IGame;
import interfaces.ILoadable;
import interfaces.IPoint;
import interfaces.ISavable;

public class GameController implements IGame, ILoadable, ISavable {
	
	public String gameTitle;
	
    protected Wall[][] topWalls;
    protected Wall[][] leftWalls;
	
	public int mazeWidthAcross;
	public int mazeDepthDown;

	public GamePoint theseusCharacter;
	public GamePoint minotaurCharacter;
	public GamePoint exitCharacter;
	
	/*
	 * start()
	 * Start's engine for game, e.g. map, characters
	 */
	public GameController() {
		System.out.println("Starting Theseus and Minotaur...");
		
		System.out.println("\nLoading maze level...");
		
		FileLoader xmlLoader = new FileLoader();
		xmlLoader.loadLevel(this);
		
		System.out.println("\n--- Loaded " + this.gameTitle + " ---");
		
		System.out.println("\nTOP WALLS:");
		
		for(int i = 0; i < this.topWalls.length; i++) {
			for(int j = 0; j < this.topWalls[i].length; j++) {
				System.out.print("  " + this.topWalls[i][j].toString("top"));
			}
			System.out.println("");
		}
		
		System.out.println("-------");
		System.out.println("LEFT WALLS:");
		
		for(int i = 0; i < this.leftWalls.length; i++) {
			for(int j = 0; j < this.leftWalls[i].length; j++) {
				System.out.print("  " + this.leftWalls[i][j].toString("left"));
			}
			System.out.println("");
		}
		
		System.out.println("\n--- Loading characters ---");
		System.out.println("\nTheseus loaded, position: " + this.theseusCharacter.toString());
		System.out.println("Minotaur loaded, position: " + this.minotaurCharacter.toString());
		System.out.println("Exit loaded, position: " + this.exitCharacter.toString());

		System.out.println("\nTesting Theseus Movement:\n");
	}
	
	public void stop() {
		System.out.println("Stopping Theseus and Minotaur...");
	}
	
	/*
	 * IGame <<Interface>>
	 * @see interfaces.IGame
	 */

	@Override
	public void moveTheseus(Direction direction) {
		boolean canMove = this.canMove(direction, theseusCharacter);
		
		if(canMove) {
			theseusCharacter.moveLocation(direction.x, direction.y);
			
			System.out.println("POSITION AFTER THESEUS MOVES "+ direction.name() +":" + theseusCharacter.toString());
		} else {
			System.out.println("Can't move " + direction.toString() + ", character blocked.");
		}
	}

	@Override
	public void moveMinotaur() {
		// TODO Auto-generated method stub
		
	}

	/*
	 * TODO: Clean up case, don't need so many function calls.
	 */
	@Override
	public boolean canMove(Direction direction, GamePoint character) {
		int[] nextLocation = character.nextLocation(direction.x, direction.y);
		
		System.out.println("POSITION BEFORE THESEUS MOVES "+ direction.name() +": " + character.toString());
		
		switch(direction) {
			case UP:
				if(this.whatsAbove(new GamePoint(nextLocation[0], nextLocation[1] + 1)) != Wall.SOMETHING) {
					return true;
				}
				break;
			case DOWN:
				if(this.whatsAbove(new GamePoint(nextLocation[0], nextLocation[1])) != Wall.SOMETHING) {
					return true;
				}
				break;
			case LEFT:
				if(this.whatsLeft(new GamePoint(nextLocation[0] + 1, nextLocation[1])) != Wall.SOMETHING) {
					return true;
				}
				break;
			case RIGHT:
				if(this.whatsLeft(new GamePoint(nextLocation[0], nextLocation[1])) != Wall.SOMETHING) {
					return true;
				}
				break;
		}
		
		return false;
	}
	
	/*
	 * ILoadable <<Interface>>
	 * @see interfaces.ILoadable
	 */

	@Override
	public void setGameTitle(String gameTitle) {
		this.gameTitle = gameTitle;
	}

	@Override
	public void setWidthAcross(int widthAcross) {
		this.mazeWidthAcross = widthAcross;
		
		/* Call reset of grid because size has changed */
		if (this.mazeDepthDown > 0) {
            this.createGrid();
        }
	}

	@Override
	public void setDepthDown(int depthDown) {
		this.mazeDepthDown = depthDown;

		/* Call reset of grid because size has changed */
		if (this.mazeWidthAcross > 0) {
            this.createGrid();
        }
	}
	
	public void createGrid() {
		this.leftWalls = new Wall[this.mazeDepthDown][this.mazeWidthAcross];
        this.topWalls = new Wall[this.mazeDepthDown][this.mazeWidthAcross];

        for (int i = 0; i < this.mazeDepthDown; ++i){
            for (int j = 0; j < this.mazeWidthAcross; j++) {
                this.topWalls[i][j] = Wall.NOTHING;
                this.leftWalls[i][j] = Wall.NOTHING;
            }
        }
	}

	@Override
	public void addWallAbove(IPoint where) {
		this.topWalls[where.getX()][where.getY()] = Wall.SOMETHING;
	}

	@Override
	public void addWallLeft(IPoint where) {
        this.leftWalls[where.getX()][where.getY()] = Wall.SOMETHING;
	}

	@Override
	public void addTheseus(IPoint characterPoint) {
		this.theseusCharacter = new GamePoint(characterPoint);
	}

	@Override
	public void addMinotaur(IPoint characterPoint) {
		this.minotaurCharacter = new GamePoint(characterPoint);
	}

	@Override
	public void addExit(IPoint characterPoint) {
		this.exitCharacter = new GamePoint(characterPoint);
	}
	
	@Override
	public int getWidthAcross() {
		return this.mazeWidthAcross;
	}

	@Override
	public int getDepthDown() {
		return this.mazeDepthDown;
	}
	
	/*
	 * ISavable <<Interface>>
	 * @see interfaces.ISavable
	 */

	/*
	 * TODO: Make a boundary for -1 values, ban em.
	 */
	@Override
	public Wall whatsAbove(IPoint where) {
		if(where.getY() == -1 
				|| where.getY() >= this.getDepthDown()) {
			return Wall.SOMETHING;
		}
		
		return topWalls[where.getY()][where.getX()];
	}

	/*
	 * TODO: Make a boundary for -1 values, ban em.
	 */
	@Override
	public Wall whatsLeft(IPoint where) {
		if(where.getX() == -1 
				|| where.getX() >= this.getWidthAcross() ) {
			return Wall.SOMETHING;
		}
		
		return leftWalls[where.getY()][where.getX()];
	}

	@Override
	public IPoint wheresTheseus() {
		return theseusCharacter;
	}

	@Override
	public IPoint wheresMinotaur() {
		return minotaurCharacter;
	}

	@Override
	public IPoint wheresExit() {
		return exitCharacter;
	}
	
}
