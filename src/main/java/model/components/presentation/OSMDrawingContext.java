package model.components.presentation;

import java.awt.Color;

public class OSMDrawingContext extends DrawingContext{
    public OSMDrawingContext() {
        initSchemata();
    }

    @Override
    protected void initSchemata()
    {
        context.put(1001,new PresentationSchema(Color.BLACK, Color.RED));
        context.put(1002,new PresentationSchema(Color.BLACK, Color.RED));
        context.put(1003,new PresentationSchema(Color.BLACK, Color.RED));
        context.put(1004,new PresentationSchema(Color.BLACK, Color.RED));
        context.put(1005,new PresentationSchema(Color.BLACK, Color.RED));
        context.put(1006,new PresentationSchema(Color.BLACK, Color.RED));
        context.put(1007,new PresentationSchema(Color.BLACK, Color.RED));
        context.put(1008,new PresentationSchema(Color.BLACK, Color.RED));
        context.put(1009,new PresentationSchema(Color.BLACK, Color.RED));
        context.put(1010,new PresentationSchema(Color.BLACK, Color.RED));
        context.put(1011,new PresentationSchema(Color.BLACK, Color.RED));
        context.put(1012,new PresentationSchema(Color.BLACK, Color.RED));
        context.put(1013,new PresentationSchema(Color.BLACK, Color.RED));
        context.put(1014,new PresentationSchema(Color.BLACK, Color.RED));
        context.put(1015,new PresentationSchema(Color.BLACK, Color.RED));
        context.put(1016,new PresentationSchema(Color.BLACK, Color.RED));
        context.put(1017,new PresentationSchema(Color.BLACK, Color.RED));
        context.put(1018,new PresentationSchema(Color.BLACK, Color.RED));
        context.put(1019,new PresentationSchema(Color.BLACK, Color.RED));
        context.put(1020,new PresentationSchema(Color.BLACK, Color.RED));
        context.put(1021,new PresentationSchema(Color.BLACK, Color.RED));
        context.put(1022,new PresentationSchema(Color.BLACK, Color.RED));
        context.put(1023,new PresentationSchema(Color.BLACK, Color.RED));
        context.put(1024,new PresentationSchema(Color.BLACK, Color.RED));
        context.put(1025,new PresentationSchema(Color.BLACK, Color.RED));
        context.put(1026,new PresentationSchema(Color.BLACK, Color.RED));
        context.put(1027,new PresentationSchema(Color.BLACK, Color.RED));
        context.put(1028,new PresentationSchema(Color.BLACK, Color.RED));
        context.put(1029,new PresentationSchema(Color.BLACK, Color.RED));
        context.put(1030,new PresentationSchema(Color.BLACK, Color.RED));
        //waterway
        context.put(2001,new PresentationSchema(Color.BLACK, Color.BLUE));
        context.put(2002,new PresentationSchema(Color.BLACK, Color.BLUE));
        context.put(2003,new PresentationSchema(Color.BLACK, Color.BLUE));
        context.put(2004,new PresentationSchema(Color.BLACK, Color.BLUE));
        context.put(2005,new PresentationSchema(Color.BLACK, Color.BLUE));
        context.put(2006,new PresentationSchema(Color.BLACK, Color.BLUE));
        context.put(2007,new PresentationSchema(Color.BLACK, Color.BLUE));
        //railway
        context.put(3001,new PresentationSchema(Color.BLACK, Color.DARK_GRAY));
        context.put(3002,new PresentationSchema(Color.BLACK, Color.DARK_GRAY));
        context.put(3003,new PresentationSchema(Color.BLACK, Color.DARK_GRAY));
        context.put(3004,new PresentationSchema(Color.BLACK, Color.DARK_GRAY));
        //leisure
        context.put(4001,new PresentationSchema(Color.BLACK, Color.GRAY));
        context.put(4002,new PresentationSchema(Color.BLACK, Color.GRAY));
        context.put(4003,new PresentationSchema(Color.BLACK, Color.GRAY));
        context.put(4004,new PresentationSchema(Color.BLACK, Color.GRAY));
        context.put(4005,new PresentationSchema(Color.BLACK, Color.GRAY));
        context.put(4006,new PresentationSchema(Color.BLACK, Color.GRAY));
        context.put(4007,new PresentationSchema(Color.BLACK, Color.GRAY));
        context.put(4008,new PresentationSchema(Color.BLACK, Color.GRAY));
        context.put(4009,new PresentationSchema(Color.BLACK, Color.GRAY));
        //landuse
        context.put(5001,new PresentationSchema(Color.BLACK, Color.GREEN));
        context.put(5002,new PresentationSchema(Color.BLACK, Color.GREEN));
        context.put(5003,new PresentationSchema(Color.BLACK, Color.GREEN));
        context.put(5004,new PresentationSchema(Color.BLACK, Color.GREEN));
        context.put(5005,new PresentationSchema(Color.BLACK, Color.GREEN));
        context.put(5006,new PresentationSchema(Color.BLACK, Color.GREEN));
        context.put(5007,new PresentationSchema(Color.BLACK, Color.GREEN));
        context.put(5008,new PresentationSchema(Color.BLACK, Color.GREEN));
        context.put(5009,new PresentationSchema(Color.BLACK, Color.GREEN));
        context.put(5010,new PresentationSchema(Color.BLACK, Color.GREEN));
        context.put(5011,new PresentationSchema(Color.BLACK, Color.GREEN));
        context.put(5012,new PresentationSchema(Color.BLACK, Color.GREEN));
        context.put(5013,new PresentationSchema(Color.BLACK, Color.GREEN));
        context.put(5014,new PresentationSchema(Color.BLACK, Color.GREEN));
        context.put(5015,new PresentationSchema(Color.BLACK, Color.GREEN));
        context.put(5016,new PresentationSchema(Color.BLACK, Color.GREEN));
        context.put(5017,new PresentationSchema(Color.BLACK, Color.GREEN));
        context.put(5018,new PresentationSchema(Color.BLACK, Color.GREEN));
        context.put(5019,new PresentationSchema(Color.BLACK, Color.GREEN));
        //natural
        context.put(6001,new PresentationSchema(Color.BLACK, Color.GREEN));
        context.put(6002,new PresentationSchema(Color.BLACK, Color.GREEN));
        context.put(6003,new PresentationSchema(Color.BLACK, Color.GREEN));
        context.put(6004,new PresentationSchema(Color.BLACK, Color.GREEN));
        context.put(6005,new PresentationSchema(Color.BLACK, Color.GREEN));
        context.put(6006,new PresentationSchema(Color.BLACK, Color.GREEN));
        context.put(6007,new PresentationSchema(Color.BLACK, Color.GREEN));
        context.put(6008,new PresentationSchema(Color.BLACK, Color.GREEN));
        context.put(6009,new PresentationSchema(Color.BLACK, Color.GREEN));
        context.put(6010,new PresentationSchema(Color.BLACK, Color.GREEN));
        context.put(6011,new PresentationSchema(Color.BLACK, Color.GREEN));
        context.put(6012,new PresentationSchema(Color.BLACK, Color.GREEN));
        context.put(6013,new PresentationSchema(Color.BLACK, Color.GREEN));
        context.put(6014,new PresentationSchema(Color.BLACK, Color.GREEN));
        context.put(6015,new PresentationSchema(Color.BLACK, Color.GREEN));
        //place
        context.put(7001,new PresentationSchema(Color.BLACK, Color.WHITE));
        context.put(7002,new PresentationSchema(Color.BLACK, Color.WHITE));
        context.put(7003,new PresentationSchema(Color.BLACK, Color.WHITE));
        context.put(7004,new PresentationSchema(Color.BLACK, Color.WHITE));
        context.put(7005,new PresentationSchema(Color.BLACK, Color.WHITE));
        context.put(7006,new PresentationSchema(Color.BLACK, Color.WHITE));
        context.put(7007,new PresentationSchema(Color.BLACK, Color.WHITE));
        context.put(7008,new PresentationSchema(Color.BLACK, Color.WHITE));
        //boundary
        context.put(8001,new PresentationSchema(Color.BLACK, Color.WHITE));
        context.put(8002,new PresentationSchema(Color.BLACK, Color.WHITE));
        context.put(8003,new PresentationSchema(Color.BLACK, Color.WHITE));
        context.put(8004,new PresentationSchema(Color.BLACK, Color.WHITE));
        context.put(8005,new PresentationSchema(Color.BLACK, Color.WHITE));
        context.put(8006,new PresentationSchema(Color.BLACK, Color.WHITE));
        context.put(8007,new PresentationSchema(Color.BLACK, Color.WHITE));
        //building
        context.put(9001,new PresentationSchema(Color.BLACK, Color.LIGHT_GRAY));
        context.put(9002,new PresentationSchema(Color.BLACK, Color.LIGHT_GRAY));
        context.put(9003,new PresentationSchema(Color.BLACK, Color.LIGHT_GRAY));
        context.put(9004,new PresentationSchema(Color.BLACK, Color.LIGHT_GRAY));
        context.put(9005,new PresentationSchema(Color.BLACK, Color.LIGHT_GRAY));
        context.put(9006,new PresentationSchema(Color.BLACK, Color.LIGHT_GRAY));
        context.put(9007,new PresentationSchema(Color.BLACK, Color.LIGHT_GRAY));
        context.put(9008,new PresentationSchema(Color.BLACK, Color.LIGHT_GRAY));
        context.put(9009,new PresentationSchema(Color.BLACK, Color.LIGHT_GRAY));
        context.put(9010,new PresentationSchema(Color.BLACK, Color.LIGHT_GRAY));
        context.put(9011,new PresentationSchema(Color.BLACK, Color.LIGHT_GRAY));
        context.put(9012,new PresentationSchema(Color.BLACK, Color.LIGHT_GRAY));
        context.put(9013,new PresentationSchema(Color.BLACK, Color.LIGHT_GRAY));
        context.put(9014,new PresentationSchema(Color.BLACK, Color.LIGHT_GRAY));
        context.put(9015,new PresentationSchema(Color.BLACK, Color.LIGHT_GRAY));
        context.put(9016,new PresentationSchema(Color.BLACK, Color.LIGHT_GRAY));
        context.put(9017,new PresentationSchema(Color.BLACK, Color.LIGHT_GRAY));
        context.put(9018,new PresentationSchema(Color.BLACK, Color.LIGHT_GRAY));
        context.put(9019,new PresentationSchema(Color.BLACK, Color.LIGHT_GRAY));
        context.put(9020,new PresentationSchema(Color.BLACK, Color.LIGHT_GRAY));
        context.put(9021,new PresentationSchema(Color.BLACK, Color.LIGHT_GRAY));
        context.put(9022,new PresentationSchema(Color.BLACK, Color.LIGHT_GRAY));
        context.put(9023,new PresentationSchema(Color.BLACK, Color.LIGHT_GRAY));
        context.put(9024,new PresentationSchema(Color.BLACK, Color.LIGHT_GRAY));
        context.put(9025,new PresentationSchema(Color.BLACK, Color.LIGHT_GRAY));
        context.put(9026,new PresentationSchema(Color.BLACK, Color.LIGHT_GRAY));
        context.put(9027,new PresentationSchema(Color.BLACK, Color.LIGHT_GRAY));
        context.put(9028,new PresentationSchema(Color.BLACK, Color.LIGHT_GRAY));
        context.put(9099,new PresentationSchema(Color.BLACK, Color.LIGHT_GRAY));
    }
}
