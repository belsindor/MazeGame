package MazeGame;

public enum UnitType {
    INFANTRY {
        @Override
        public double damageTo(UnitType defender) {
            return defender == ARCHER ? 1.5 : 1.0;
        }
    },
    ARCHER {
        @Override
        public double damageTo(UnitType defender) {
            return defender == FLYING ? 1.5 : 1.0;
        }
    },
    FLYING {
        @Override
        public double damageTo(UnitType defender) {
            return defender == INFANTRY ? 1.5 : 1.0;
        }
    },
    NONE {
        @Override
        public double damageTo(UnitType defender) {
            return 1.001;
        }
    };

    public double damageTo(UnitType defender) {
        return 1.0;
    }
}
