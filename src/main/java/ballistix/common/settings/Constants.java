package ballistix.common.settings;

import electrodynamics.api.configuration.BooleanValue;
import electrodynamics.api.configuration.Configuration;
import electrodynamics.api.configuration.DoubleValue;
import electrodynamics.api.configuration.IntValue;

@Configuration(name = "Ballistix")
public class Constants {
	@DoubleValue(def = 45.0)
	public static double EXPLOSIVE_ANTIMATTER_RADIUS = 45.0;
	@DoubleValue(def = 20.0, comment = "Value is in ticks.")
	public static double EXPLOSIVE_ANTIMATTER_DURATION = 10.0;
	@DoubleValue(def = 45.0)
	public static double EXPLOSIVE_EMP_RADIUS = 45.0;
	@DoubleValue(def = 80.0)
	public static double EXPLOSIVE_LARGEANTIMATTER_RADIUS = 80.0;
	@DoubleValue(def = 200.0, comment = "Value is in ticks.")
	public static double EXPLOSIVE_LARGEANTIMATTER_DURATION = 30.0;
	@DoubleValue(def = 1.0)
	public static double EXPLOSIVE_ATTRACTIVE_SIZE = 1.0;
	@DoubleValue(def = 1.0)
	public static double EXPLOSIVE_REPULSIVE_SIZE = 1.0;
	@DoubleValue(def = 2.0)
	public static double EXPLOSIVE_ATTRACTIVE_REPULSIVE_PUSH_STRENGTH = 2.0;
	@DoubleValue(def = 10.0)
	public static double EXPLOSIVE_BREACHING_SIZE = 10.0;
	@DoubleValue(def = 7.0)
	public static double EXPLOSIVE_CHEMICAL_SIZE = 7.0;
	@DoubleValue(def = 1200.0, comment = "Value is in ticks.")
	public static double EXPLOSIVE_CHEMICAL_DURATION = 1200.0;
	@DoubleValue(def = 2.5)
	public static double EXPLOSIVE_CONDENSIVE_SIZE = 2.5;
	@DoubleValue(def = 7.0)
	public static double EXPLOSIVE_CONTAGIOUS_SIZE = 7.0;
	@DoubleValue(def = 1200.0, comment = "Value is in ticks.")
	public static double EXPLOSIVE_CONTAGIOUS_DURATION = 1200.0;
	@DoubleValue(def = 50.0)
	public static double EXPLOSIVE_DARKMATTER_RADIUS = 50.0;
	@DoubleValue(def = 1200.0, comment = "Value is in ticks.")
	public static double EXPLOSIVE_DARKMATTER_DURATION = 1200.0;
	@DoubleValue(def = 7.0)
	public static double EXPLOSIVE_DEBILITATION_SIZE = 7.0;
	@DoubleValue(def = 1200.0, comment = "Value is in ticks.")
	public static double EXPLOSIVE_DEBILITATION_DURATION = 1200.0;
	@DoubleValue(def = 50.0)
	public static double EXPLOSIVE_FRAGMENTATION_SHRAPNEL_COUNT = 50.0;
	@DoubleValue(def = 7.0)
	public static double EXPLOSIVE_INCENDIARY_RADIUS = 7.0;
	@DoubleValue(def = 35.0)
	public static double EXPLOSIVE_NUCLEAR_SIZE = 35.0;
	@DoubleValue(def = 30.0, comment = "Value is in ticks.")
	public static double EXPLOSIVE_NUCLEAR_DURATION = 30.0;
	@DoubleValue(def = 80.0)
	public static double EXPLOSIVE_NUCLEAR_ENERGY = 80.0;
	@DoubleValue(def = 10.0)
	public static double EXPLOSIVE_OBSIDIAN_SIZE = 10.0;
	@DoubleValue(def = 25.0)
	public static double EXPLOSIVE_SHRAPNEL_SHRAPNEL_COUNT = 25.0;
	@DoubleValue(def = 20.0)
	public static double EXPLOSIVE_THERMOBARIC_SIZE = 20.0;
	@DoubleValue(def = 15.0, comment = "Value is in ticks.")
	public static double EXPLOSIVE_THERMOBARIC_DURATION = 15.0;
	@DoubleValue(def = 45.0)
	public static double EXPLOSIVE_THERMOBARIC_ENERGY = 45.0;
	@DoubleValue(def = 10000, comment = "Usage per launch in joules")
	public static double MISSILESILO_USAGE = 10000;
	@DoubleValue(def = 10000, comment = "Usage in watt for radar")
	public static double RADAR_USAGE = 10000;

	@IntValue(def = 3000, comment = "Set to -1 for unlimited range")
	public static int CLOSERANGE_MISSILE_RANGE = 3000;
	@IntValue(def = 10000, comment = "Set to -1 for unlimited range")
	public static int MEDIUMRANGE_MISSILE_RANGE = 10000;
	@IntValue(def = -1, comment = "Set to -1 for unlimited range")
	public static int LONGRANGE_MISSILE_RANGE = -1;
	@BooleanValue(def = false, comment = "Should explosions bypass claims")
	public static boolean SHOULD_EXPLOSIONS_BYPASS_CLAIMS = false;

}
