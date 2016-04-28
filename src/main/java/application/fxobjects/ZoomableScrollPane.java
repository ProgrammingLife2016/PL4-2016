package application.fxobjects;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;


public class ZoomableScrollPane extends ScrollPane {
    Group zoomGroup;
    Scale scaleTransform;
    Node content;
    double scaleValue = 0.1;

    private Rectangle zoomRectBorder;
    public Rectangle zoomRect;


    public ZoomableScrollPane(Node content) {
        this.content = content;
//        this.setHbarPolicy(ScrollBarPolicy.NEVER);
//        this.setVbarPolicy(ScrollBarPolicy.NEVER);

        Group contentGroup = new Group();


        zoomGroup = new Group();
        contentGroup.getChildren().addAll(zoomGroup);
        zoomGroup.getChildren().add(content);
        setContent(contentGroup);
        scaleTransform = new Scale(scaleValue, scaleValue, 0, 0);
        zoomGroup.getTransforms().add(scaleTransform);

    }


    public double getScaleValue() {
        return scaleValue;
    }

   // public void zoomToActual() {
    //    controller.zoomTo(1.0);
  //  }

    public void zoomTo(double scaleValue) {

        this.scaleValue = scaleValue;

        scaleTransform.setX(scaleValue);
        scaleTransform.setY(scaleValue);

    }

//    public void zoomActual() {
//
//        scaleValue = 1;
//        controller.zoomTo(scaleValue);
//
//    }

//    public void zoomOut() {
//        scaleValue -= delta;
//
//        if (Double.compare(scaleValue, 0.1) < 0) {
//            scaleValue = 0.1;
//        }
//
//        controller.zoomTo(scaleValue);
//    }

//    public void zoomIn() {
//
//        scaleValue += delta;
//
//        if (Double.compare(scaleValue, 10) > 0) {
//            scaleValue = 10;
//        }
//
//        controller.zoomTo(scaleValue);
//
//    }

    public void zoom(double delta) {
        if (delta < 0.0) {
            scaleValue -= -(delta / 200);
            if (scaleValue < 0.1) {
                scaleValue = 0.1;
            }
            zoomTo(scaleValue);

        } else {
            scaleValue += (delta / 200);
            if (scaleValue > 1)
                scaleValue = 1;
            zoomTo(scaleValue);

        }
    }

    public Boolean checkRectBoundaries(double offsetX, double offsetY) {
        Boolean res = true;
        if (offsetX < 0) {
            res = (zoomRect.getX() + offsetX) >= zoomRectBorder.getX();
        } else if (offsetX > 0) {
            res = (zoomRect.getX() + zoomRect.getWidth() + offsetX)
                    <= (zoomRectBorder.getX() + zoomRectBorder.getWidth());
        }

        if (res && offsetY < 0) {
            res = (zoomRect.getY() + offsetY) >= zoomRectBorder.getY();
        } else if (res && offsetY > 0) {
            res = (zoomRect.getY() + zoomRect.getHeight() + offsetY)
                    <= (zoomRectBorder.getY() + zoomRectBorder.getHeight());
        }

        System.out.println(res);

        return res;
    }

    /**
     * @param minimizeOnly If the content fits already into the viewport, then we don't
     *                     zoom if this parameter is true.
     */
    public void zoomToFit(boolean minimizeOnly) {

        double scaleX = getViewportBounds().getWidth() / getContent().getBoundsInLocal().getWidth();
        double scaleY = getViewportBounds().getHeight() / getContent().getBoundsInLocal().getHeight();

        // consider current scale (in content calculation)
        scaleX *= scaleValue;
        scaleY *= scaleValue;

        // distorted zoom: we don't want it => we search the minimum scale
        // factor and apply it
        double scale = Math.min(scaleX, scaleY);

        // check precondition
        if (minimizeOnly) {

            // check if zoom factor would be an enlargement and if so, just set
            // it to 1
            if (Double.compare(scale, 1) > 0) {
                scale = 1;
            }
        }

        // apply zoom
        //controller.zoomTo(scale);

    }


}