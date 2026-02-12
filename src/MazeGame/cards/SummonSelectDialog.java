//package MazeGame.cards;
//
//import javax.swing.*;
//import java.awt.*;
//import java.util.Map;
//import MazeGame.UnitType;
//
//public class SummonSelectDialog extends JDialog {
//
//    private SummonCard selected = null;
//
//    public SummonSelectDialog(Window owner, SummonDeck deck) {
//        super(owner, "Выбор призыва для боя", ModalityType.APPLICATION_MODAL);
//        setSize(700, 500); setLocationRelativeTo(owner);
//        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
//
//        // Основной контейнер
//        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
//        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
//
//        // Панель с карточками (FlowLayout + возможность скролла)
//        JPanel cardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
//        cardsPanel.setOpaque(false);
//
//        // Получаем все доступные активные суммоны
//        Map<UnitType, SummonCard> active = deck.getActiveSummons();
//
//        if (active.isEmpty()) {
//            JLabel message = new JLabel("У вас нет доступных призывов");
//            message.setFont(new Font("Arial", Font.PLAIN, 20));
//            message.setHorizontalAlignment(SwingConstants.CENTER);
//            cardsPanel.add(message);
//        } else {
//            for (SummonCard card : active.values()) {
//                CardPanel panel = new CardPanel(card);
//                // Делаем карточку кликабельной через существующий метод setOnClick
//                panel.setOnClick(() -> {
//                    selected = card; dispose(); // закрываем диалог после выбора
//                });
//                // Визуальная обратная связь при наведении
//                panel.addMouseListener(new java.awt.event.MouseAdapter() {
//                    @Override
//                    public void mouseEntered(java.awt.event.MouseEvent e) {
//                        panel.setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0), 4));
//                    }
//                    @Override
//                    public void mouseExited(java.awt.event.MouseEvent e) {
//                        panel.setBorder(null);
//                    }
//                });
//
//                // Если этот суммон уже выбран — выделяем его
//                if (card == deck.getSelectedSummon()) {
//                    panel.setBorder(BorderFactory.createLineBorder(new Color(0, 180, 0), 5));
//                } cardsPanel.add(panel);
//            }
//        }
//        // Нижняя панель с кнопкой "Отмена"
//        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
//        JButton cancelButton = new JButton("Отмена");
//        cancelButton.setFont(new Font("Arial", Font.BOLD, 16));
//        cancelButton.setPreferredSize(new Dimension(140, 45));
//        cancelButton.addActionListener(e -> dispose());
//        bottomPanel.add(cancelButton);
//
//        // Скролл, если карточек много
//        JScrollPane scrollPane = new JScrollPane(cardsPanel);
//        scrollPane.setBorder(null);
//        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
//        mainPanel.add(scrollPane, BorderLayout.CENTER);
//        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
//        setContentPane(mainPanel);
//    }
//    /** * Возвращает выбранный игроком суммон (или null, если выбор отменён) */
//
//    public SummonCard getSelected() { return selected; }
//}