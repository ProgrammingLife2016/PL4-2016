package application.fxobjects.cell.graph;
import application.fxobjects.cell.Cell;
import core.graph.cell.CellType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

/**
 * Class representing a collapsed bubble.
 */
public class BubbleCell extends Cell {
    private final CellType type = CellType.BUBBLE;
    private Shape shape;

    /**
     * Bubble cell constructor.
     * @param id            The ID of a cell.
     * @param nucleotides The amount of nucleotides contained in this cell.
     * @param collapseLevel The collapse level of this cell.
     */
    public BubbleCell(int id, int nucleotides, String collapseLevel) {
        this(id, nucleotides, new StackPane(), new Text(collapseLevel));
    }

    /**
     * Bubble cell constructor.
     *
     * @param id   The ID of a cell.
     * @param nucleotides The amount of nucleotides contained in this cell.
     * @param pane A given stack pane.
     * @param text A given text element.
     */
    public BubbleCell(int id, int nucleotides, StackPane pane, Text text) {
        super(id);
        shape = new Circle(Math.min(10.0 + ((double) nucleotides) / 80000, 100));
        shape.setStroke(Color.YELLOW);
        shape.setStrokeWidth(1);
        shape.setFill(Color.YELLOW);
        pane.getChildren().addAll(shape, text);
        setView(pane);
    }

    /**
     * Return the type of the Cell.
     *
     * @return the type of the Cell.
     */
    public CellType getType() {
        return type;
    }

    /**
     * Returns the cellshape.
     * @return the cellshape.
     */
    public Shape getCellShape() { return shape; }

    /**
     * Method to set the focus.
     */
    public void focus() {
        DropShadow borderGlow = new DropShadow();
        borderGlow.setOffsetY(0f);
        borderGlow.setOffsetX(0f);
        borderGlow.setColor(Color.BLACK);
        borderGlow.setWidth(70);
        borderGlow.setHeight(70);
        this.setEffect(borderGlow);

        shape.setStroke(Color.PURPLE);
        shape.setStrokeWidth(4);
    }

    /**
     * Method to set the side focus.
     */
    public void sideFocus() {
        DropShadow borderGlow = new DropShadow();
        borderGlow.setOffsetY(0f);
        borderGlow.setOffsetX(0f);
        borderGlow.setColor(Color.BLACK);
        borderGlow.setWidth(40);
        borderGlow.setHeight(40);
        this.setEffect(borderGlow);
        shape.setStroke(Color.MEDIUMPURPLE);
        shape.setStrokeWidth(4);
    }

    /**
     * Method to reset the focus.
     */
    public void resetFocus() {
        this.setEffect(null);
        shape.setStroke(Color.YELLOW);
        shape.setStrokeWidth(1);
    }
}