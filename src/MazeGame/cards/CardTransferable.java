package MazeGame.cards;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

public class CardTransferable implements Transferable {

    public static final DataFlavor CARD_FLAVOR =
            new DataFlavor(Card.class, "Card");

    private final Card card;

    public CardTransferable(Card card) {
        this.card = card;
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[]{CARD_FLAVOR};
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return CARD_FLAVOR.equals(flavor);
    }

    @Override
    public Object getTransferData(DataFlavor flavor) {
        return card;
    }
}

