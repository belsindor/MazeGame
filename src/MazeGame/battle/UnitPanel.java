package MazeGame.battle;

import MazeGame.Monster;
import MazeGame.battle.effects.AbstractBattleEffect;
import MazeGame.battle.effects.BattleEffect;
import MazeGame.cards.Card;
import MazeGame.cards.CardTarget;
import MazeGame.cards.CardTransferable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UnitPanel extends JPanel {

    private final BattleUnit unit;
    private final BattleSide side;
    private final JLabel nameLabel;
    private final JProgressBar hpBar;
    private final JLabel imageLabel;  // ← новая метка для картинки
    private final CardTarget target;
    private final JPanel effectsPanel;


    public UnitPanel(BattleSide side, String title, Color borderColor, CardTarget target) {
        this.side = side;
        this.unit = side.getUnit();
        this.target = target;


        setTransferHandler(new TransferHandler() {

            @Override
            public boolean canImport(TransferSupport support) {
                return support.isDataFlavorSupported(CardTransferable.CARD_FLAVOR);
            }

            @Override
            public boolean importData(TransferSupport support) {
                try {
                    Card card = (Card) support.getTransferable()
                            .getTransferData(CardTransferable.CARD_FLAVOR);

                    BattleWindow window =
                            (BattleWindow) SwingUtilities.getWindowAncestor(UnitPanel.this);

                    window.onCardDropped(card, target);
                    return true;

                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
        });

        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(BorderFactory.createLineBorder(borderColor, 2));
        setPreferredSize(new Dimension(300, 400));

        // Верх: имя и уровень
        nameLabel = new JLabel(title + " (Lv. " + unit.getLevel() + ")", SwingConstants.CENTER);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        nameLabel.setForeground(Color.WHITE);
        add(nameLabel, BorderLayout.NORTH);

        // Центр: картинка юнита
        // Центр: контейнер с наложением
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new OverlayLayout(centerPanel));
        centerPanel.setOpaque(false);
        add(centerPanel, BorderLayout.CENTER);

        // Панель эффектов поверх картинки
        effectsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 4));
        effectsPanel.setOpaque(false);
        effectsPanel.setAlignmentX(100.0f);  // слева
        effectsPanel.setAlignmentY(100.0f);  // сверху
        centerPanel.add(effectsPanel);

        // Картинка юнита
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setAlignmentX(0.5f);
        imageLabel.setAlignmentY(0.5f);
        centerPanel.add(imageLabel);





        // Низ: красная полоска HP
        hpBar = new JProgressBar(0, unit.getMaxHealth());
        hpBar.setValue(unit.getHealth());
        hpBar.setStringPainted(true);
        hpBar.setString(unit.getHealth() + " / " + unit.getMaxHealth());
        hpBar.setForeground(new Color(220, 60, 60));  // ← красный цвет!
        hpBar.setBackground(new Color(50, 50, 50));
        hpBar.setPreferredSize(new Dimension(300, 30));
        add(hpBar, BorderLayout.SOUTH);

        // Tooltip с подробностями
        setToolTipText(buildTooltip(unit));

        // Загружаем картинку (если юнит — Monster или Summon)
        loadUnitImage();
    }


    public void updateEffects() {
        effectsPanel.removeAll();

        for (BattleEffect effect : side.getEffects()) {
            JLabel icon = new JLabel();
            if (effect instanceof AbstractBattleEffect abe &&
                    abe.getIconPath() != null) {

                var url = getClass().getResource(abe.getIconPath());

                if (url != null) {
                    ImageIcon original = new ImageIcon(url);
                    Image scaled = original.getImage()
                            .getScaledInstance(32, 48, Image.SCALE_SMOOTH);

                    icon.setIcon(new ImageIcon(scaled));
                }
            } else {
                icon.setText("🟣");
            }

            icon.setToolTipText(effect.getName()
                    + " (" + effect.getRemainingTurns() + " ход.)");

            effectsPanel.add(icon);
        }

        effectsPanel.revalidate();
        effectsPanel.repaint();
    }


    /**
     * Загрузка картинки монстра или суммона
     */
    private void loadUnitImage() {
        String path = null;

        if (unit instanceof Monster monster) {
            path = monster.getImagePath();
        } else if (unit instanceof Monster) {  // если суммон тоже Monster
            path = ((Monster) unit).getImagePath();
        }

        if (path != null) {
            var url = getClass().getResource(path);
            if (url != null) {
                ImageIcon original = new ImageIcon(url);
                Image scaled = original.getImage().getScaledInstance(200, 300, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaled));
            } else {
                System.err.println("Картинка не найдена: " + path);
                imageLabel.setText("Нет изображения");
                imageLabel.setForeground(Color.GRAY);
            }
        } else {
            imageLabel.setText("Нет изображения");
            imageLabel.setForeground(Color.GRAY);
        }
    }

    public void update() {
        hpBar.setValue(unit.getHealth());
        hpBar.setString(unit.getHealth() + " / " + unit.getMaxHealth());
    }

    private String buildTooltip(BattleUnit unit) {
        StringBuilder sb = new StringBuilder("<html>");
        sb.append("<b>").append(unit.getName()).append("</b><br>");
        sb.append("Уровень: ").append(unit.getLevel()).append("<br>");
        sb.append("Здоровье: ").append(unit.getHealth()).append(" / ").append(unit.getMaxHealth()).append("<br>");
        sb.append("Атака: ").append(unit.getTotalAttack()).append("<br>");
        sb.append("Защита: ").append(unit.getTotalDefense()).append("<br>");
        sb.append("</html>");
        return sb.toString();
    }

    // Методы для анимации (вспышка и дрожание)
    public void flash(Color color, int durationMs) {
        setBackground(color);
        Timer timer = new Timer(durationMs, e -> setBackground(null));
        timer.setRepeats(false);
        timer.start();
    }

    public void shake(int intensity, int durationMs) {
        Point original = getLocation();
        Timer timer = new Timer(30, new ActionListener() {
            int step = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                step++;
                int shake = (step % 4 - 2) * intensity;
                setLocation(original.x + shake, original.y);

                if (step * 30 >= durationMs) {
                    ((Timer) e.getSource()).stop();
                    setLocation(original);
                }
            }
        });
        timer.start();
    }

    // Переопределения для совместимости
    @Override
    public int getWidth() {
        return super.getWidth();
    }

    @Override
    public int getHeight() {
        return super.getHeight();
    }
}