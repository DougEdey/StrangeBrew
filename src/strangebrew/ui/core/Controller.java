package strangebrew.ui.core;

public abstract class Controller {

	View myView;
	
	public Controller(View aView) {
		myView = aView;
		aView.setController(this);
	}
	public abstract void init();
	public abstract void dispose();
	public abstract void execute();
	
}
