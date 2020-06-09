package sim.subd;

public enum Teams {


    MERCEDES{
        @Override
        public Double getSpeed(){ return 280.5; }
    }, FERRARI{
        @Override
        public Double getSpeed(){ return 280.0; }
    }, RED_BULL{
        @Override
        public Double getSpeed(){ return 279.8; }
    }, McLARREN{
        @Override
        public Double getSpeed(){ return 279.0; }
    }, ALFA_ROMEO{
        @Override
        public Double getSpeed(){ return 278.6; }
    }, RENAULT{
        @Override
        public Double getSpeed(){ return 278.0; }
    }, RACING_POINT{
        @Override
        public Double getSpeed(){ return 277.0; }
    }, WILLIAMS{
        @Override
        public Double getSpeed(){ return 276.5; }
    }, TORO_ROSSO{
        @Override
        public Double getSpeed(){ return 277.3; }
    }, HAAS{
        @Override
        public Double getSpeed(){ return 276.7; }
    };
    public abstract Double getSpeed();

}
