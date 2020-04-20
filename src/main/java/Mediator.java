import Entities.Project;
import Entities.Team;
import Entities.User;

/*** This singleton class is used to set application variables and pass information onto other controllers
     via the Controller object stack. It essentially lets the current view know which controller
     created it allowing for backtracking to the previous controller with possible important data.
     Since certain views can be created and inflated by multiple controllers, it is important to know
     which controller to go back to on various actions.
 ***/

public class Mediator implements OnFXMLChangedListener {

    private static Mediator mediator = null;
    private final MediatorStack<Controller> controllers;

    private User currentUser;
    private Project currentProject;
    private Team currentTeam;

    private Mediator() {
        controllers = new MediatorStack<>();
    }

    public static Mediator getInstance() {
        if (mediator == null)
            mediator = new Mediator();
        return mediator;
    }

    @Override
    public void onControllerLoadFXML(String FXML) {
        System.out.println("onControllerLoadFXML");
        if (controllers.peek() instanceof MainController) {
            ((MainController) controllers.peek()).onControllerLoadFXML(FXML);
        }
        if (controllers.peek() instanceof HomeController) {
            ((HomeController) controllers.peek()).onControllerLoadFXML(FXML);
        }
    }

    public Controller getParentController() {
        return controllers.peek();
    }

    public void setParentController(Controller parentController) {
        controllers.push(parentController);
    }

    public void removeParentController() {
        controllers.pop();
    }

    public Project getCurrentProject() {
        return currentProject;
    }

    public void setCurrentProject(Project currentProject) {
        this.currentProject = currentProject;
    }

    public Team getCurrentTeam() {
        return currentTeam;
    }

    public void setCurrentTeam(Team currentTeam) {
        this.currentTeam = currentTeam;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }


    /******** stack for parent controller. Could potentially be used for other objects as well ********/
    static class MediatorStack<T> {
        private Node<T> top;

        public MediatorStack() {
            top = null;
        }

        public boolean isEmpty() {
            return top == null;
        }

        public void push(T controller) {
            Node<T> newNode = new Node<>(controller);
            if (top != null) {
                top.setNext(newNode);
                newNode.setLast(top);
            }
            top = newNode;
        }

        public T pop() {
            if (top != null) {
                Node<T> temp = top;
                if (temp.getLast() != null) {
                    top = temp.getLast();
                    top.setNext(null);
                } else
                    top = null;
                return temp.getController();
            } else return null;
        }

        public T peek() {
            return top.getController();
        }

        static class Node<T> {
            private T controller;
            private Node<T> next, last;

            public Node(T controller) {
                this.controller = controller;
            }

            public T getController() {
                return controller;
            }

            public void setController(T controller) {
                this.controller = controller;
            }

            public Node<T> getNext() {
                return next;
            }

            public void setNext(Node<T> next) {
                this.next = next;
            }

            public Node<T> getLast() {
                return last;
            }

            public void setLast(Node<T> last) {
                this.last = last;
            }
        }
    }
}
