public class ControllerHandler implements OnFXMLChangedListener {
    private Controller parentController;

    private static class ControllerInstance {
        private static final ControllerHandler handler = new ControllerHandler();
    }

    public static ControllerHandler getInstance() {
        return ControllerInstance.handler;
    }

    @Override
    public void setParentController(Controller parentController) {
        this.parentController = parentController;
    }

    @Override
    public void onControllerLoadFXML(String FXML) {
        if (parentController instanceof MainController) {
            ((MainController) parentController).onControllerLoadFXML(FXML);
        }
        if (parentController instanceof HomeController) {
            ((HomeController) parentController).onControllerLoadFXML(FXML);
        }
    }

    public Controller getParentController() {
        return parentController;
    }
}
