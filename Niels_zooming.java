
public class MainController {
    private Rectangle2D screenSize;
    private double windowWidth;
    private double windowHeight;
    private double graphBoxWidth;
    private double graphBoxHeight;
    private double zoomBoxWidth;
    private double zoomBoxHeight;

    private Rectangle zoomRectBorder;
    private Rectangle zoomRect;
    private Rectangle graphRect;

    public void launch(Stage primaryStage) {
        initVariables();

        Scene scene = new Scene(makeRoot(), windowWidth, windowHeight);
        scene.setOnKeyPressed(keyListener);
        scene.setOnScroll(scrollListener);

        primaryStage.setTitle("Zooming POC");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initVariables() {
        screenSize = Screen.getPrimary().getVisualBounds();
        windowWidth = screenSize.getWidth();
        windowHeight = screenSize.getHeight();
        graphBoxWidth = windowWidth - 10;
        graphBoxHeight = windowHeight - 10;
        zoomBoxWidth = graphBoxWidth / 5.0;
        zoomBoxHeight = graphBoxHeight / 5.0;
    }

    private Group makeRoot() {
        Group root = new Group();

        double rectX = windowWidth - zoomBoxWidth - 20;

        zoomRectBorder = new Rectangle(rectX, 20, zoomBoxWidth, zoomBoxHeight);
        zoomRectBorder.setFill(Color.WHITE);
        zoomRectBorder.setStroke(Color.LIGHTGREY);
        zoomRectBorder.setStrokeWidth(3);

        zoomRect = new Rectangle(rectX, 20, zoomBoxWidth, zoomBoxHeight);
        zoomRect.setFill(Color.TRANSPARENT);
        zoomRect.setStroke(Color.BLACK);
        zoomRect.setStrokeWidth(3);

        graphRect = new Rectangle(10, 10, graphBoxWidth - 10, graphBoxHeight - 10);
        graphRect.setFill(Color.WHITE);
        graphRect.setStroke(Color.BLACK);
        graphRect.setStrokeWidth(3);

        root.getChildren().addAll(graphRect, zoomRectBorder, zoomRect);
        return root;
    }
=======


private EventHandler<ScrollEvent> scrollListener = new EventHandler<ScrollEvent>() {
        public void handle(ScrollEvent event) {
            double delta = event.getDeltaY();

            if (delta > 0) {
                // Both positive and negative offsets have to be tested as scaleZoomRect
                // will enlarge zoomRect in all directions.
                if (checkRectBoundaries(delta, (zoomRect.getHeight() / zoomRect.getWidth()) * delta) && checkRectBoundaries(-delta, -(zoomRect.getHeight() / zoomRect.getWidth()) * delta)) {
                    scaleZoomRect(delta);
                }
            } else if (delta < 0) {
                if ((zoomRect.getHeight() + delta) >= (zoomRectBorder.getHeight() * 0.05)
                        && zoomRect.getWidth() + delta * (zoomRect.getWidth() /
                        zoomRect.getHeight()) >= (zoomRectBorder.getWidth() * 0.05)) {
                    scaleZoomRect(delta);
                }
            }
        }
    };


      private void scaleZoomRect(double delta) {
        double adj = delta * (zoomRect.getHeight() / zoomRect.getWidth());
        zoomRect.setWidth(zoomRect.getWidth() + delta);
        zoomRect.setHeight(zoomRect.getHeight() + adj);

        zoomRect.setX(zoomRect.getX() - 0.5 * delta);
        zoomRect.setY(zoomRect.getY() - 0.5 * adj);
    }

    private Boolean checkRectBoundaries(double offsetX, double offsetY) {
        Boolean res = true;
        if (res && offsetX < 0) {
            res = (zoomRect.getX() + offsetX) >= zoomRectBorder.getX();
        } else if (res && offsetX > 0) {
            res = (zoomRect.getX() + zoomRect.getWidth() + offsetX)
                    <= (zoomRectBorder.getX() + zoomRectBorder.getWidth());
        }

        if (res && offsetY < 0) {
            res = (zoomRect.getY() + offsetY) >= zoomRectBorder.getY();
        } else if (res && offsetY > 0) {
            res = (zoomRect.getY() + zoomRect.getHeight() + offsetY)
                    <= (zoomRectBorder.getY() + zoomRectBorder.getHeight());
        }

        <<<<<<< Updated upstream
        public void handle(KeyEvent event) {
            switch(event.getCode()) {
                case A:
                    if (checkRectBoundaries(-offset, 0)) {
                        zoomRect.setX(zoomRect.getX() - offset);
                    }
                    break;
                case D:
                    if (checkRectBoundaries(offset, 0)) {
                        zoomRect.setX(zoomRect.getX() + offset);
                    }
                    break;
                case W:
                    if (checkRectBoundaries(0, -offset)) {
                        zoomRect.setY(zoomRect.getY() - offset);
                    }
                    break;
                case S:
                    if (checkRectBoundaries(0, offset)) {
                        zoomRect.setY(zoomRect.getY() + offset);
                    }
                    break;
                default:
                    break;
            }
        }
    };
=======