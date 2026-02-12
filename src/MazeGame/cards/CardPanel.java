package MazeGame.cards;

import MazeGame.MonsterTemplate;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Transferable;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

/**
 * Универсальная панель карточки.
 * Может быть:
 *  - кликабельной
 *  - перетаскиваемой (drag)
 *  - отключённой (disabled)
 */
public class CardPanel extends JPanel {

    private static final int DEFAULT_WIDTH = 120;
    private static final int DEFAULT_HEIGHT = 180;

    private final Card card;
    private final boolean disabled;
    private final boolean draggable;

    /* ---------- КОНСТРУКТОРЫ ---------- */

    // Обычная карточка (клик, без drag)
    public CardPanel(Card card) {
        this(card, false, false);
    }

    // Карточка для боя (может быть disabled, drag включён)
    public CardPanel(Card card, boolean disabled) {
        this(card, disabled, true);
    }

    // Полный конструктор
    public CardPanel(Card card, boolean disabled, boolean draggable) {
        this.card = card;
        this.disabled = disabled;
        this.draggable = draggable;

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
        setOpaque(false);
        setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        if (disabled) {
            setEnabled(false);
            setOpaque(true);
            setBackground(new Color(0, 0, 0, 120));
        }

        // ---------- Drag & Drop ----------
        if (draggable) {
            setTransferHandler(new TransferHandler() {

                @Override
                protected Transferable createTransferable(JComponent c) {
                    return new CardTransferable(card);
                }

                @Override
                public int getSourceActions(JComponent c) {
                    return MOVE;
                }

                @Override
                public void exportAsDrag(JComponent comp, InputEvent e, int action) {
                    if (card == null) return;

                    Image img = getScaledImageIcon(card).getImage();
                    setDragImage(img);

                    super.exportAsDrag(comp, e, action);
                }
            });

            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if (disabled) return;
                    if (card == null || card.getId() == 0) return;

                    getTransferHandler().exportAsDrag(
                            CardPanel.this, e, TransferHandler.MOVE
                    );
                }
            });
        }

        // ---------- КАРТИНКА ----------
        JLabel imageLabel = new JLabel(getScaledImageIcon(card));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(imageLabel, BorderLayout.CENTER);

        // ---------- TOOLTIP ----------
        setToolTipText(buildTooltip(card));
    }

    /* ---------- PUBLIC API ---------- */

    public void setOnClick(Runnable action) {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (disabled) return;
                action.run();
            }
        });
    }

    public Card getCard() {
        return card;
    }

    /* ---------- PRIVATE ---------- */

    private String buildTooltip(Card card) {
        if (card == null) {
            return "<html><b>Нет карты</b></html>";
        }

        StringBuilder sb = new StringBuilder("<html>");

        sb.append("<b>").append(card.getName()).append("</b><br>");

        if (card instanceof SummonCard summon) {
            MonsterTemplate m = summon.getMonsterTemplate();
            sb.append("Уровень: ").append(m.level()).append("<br>");
            sb.append("HP: ").append(m.maxHealth()).append("<br>");
            sb.append("ATK: ").append(m.attack())
                    .append(" • DEF: ").append(m.defense()).append("<br>");
            sb.append("Тип: ").append(m.unitType());

        }

        sb.append("</html>");
        return sb.toString();
    }

    private ImageIcon getScaledImageIcon(Card card) {
        if (card == null || card.getImagePath() == null) {
            return createPlaceholderIcon();
        }

        var url = getClass().getResource(card.getImagePath());
        if (url == null) {
            System.err.println("Картинка не найдена: " + card.getImagePath());
            return createPlaceholderIcon();
        }

        ImageIcon original = new ImageIcon(url);
        Image scaled = original.getImage().getScaledInstance(
                DEFAULT_WIDTH,
                DEFAULT_HEIGHT,
                Image.SCALE_SMOOTH
        );
        return new ImageIcon(scaled);
    }

    private ImageIcon createPlaceholderIcon() {
        BufferedImage img = new BufferedImage(
                DEFAULT_WIDTH, DEFAULT_HEIGHT, BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D g = img.createGraphics();
        g.setColor(new Color(40, 40, 40));
        g.fillRect(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        g.setColor(Color.GRAY);
        g.drawRect(5, 5, DEFAULT_WIDTH - 20, DEFAULT_HEIGHT - 10);
        g.dispose();

        return new ImageIcon(img);
    }
}
