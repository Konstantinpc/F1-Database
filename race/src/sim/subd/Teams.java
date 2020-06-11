package sim.subd;

public enum Teams {


    Mercedes{
        @Override
        public Double getSpeed(){ return 280.5; }
    }, Ferrari{
        @Override
        public Double getSpeed(){ return 280.0; }
    }, Red_bull{
        @Override
        public Double getSpeed(){ return 279.8; }
    }, McLarren{
        @Override
        public Double getSpeed(){ return 279.0; }
    }, Alfa_Romeo{
        @Override
        public Double getSpeed(){ return 278.6; }
    }, Renault{
        @Override
        public Double getSpeed(){ return 278.0; }
    }, Racing_Point{
        @Override
        public Double getSpeed(){ return 277.0; }
    }, Williams{
        @Override
        public Double getSpeed(){ return 276.5; }
    }, Toro_Rosso{
        @Override
        public Double getSpeed(){ return 277.3; }
    }, Haas{
        @Override
        public Double getSpeed(){ return 276.7; }
    };
    public abstract Double getSpeed();

}
