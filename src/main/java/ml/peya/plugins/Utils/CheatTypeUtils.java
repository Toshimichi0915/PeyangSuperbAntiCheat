package ml.peya.plugins.Utils;

import ml.peya.plugins.Enum.*;

import java.util.*;

public class CheatTypeUtils
{
    public static EnumCheatType[] getFullType()
    {
        ArrayList<EnumCheatType> types = new ArrayList<>();

        types.add(EnumCheatType.FLY);
        types.add(EnumCheatType.KILLAURA);
        types.add(EnumCheatType.AUTOCLICKER);
        types.add(EnumCheatType.SPEED);
        types.add(EnumCheatType.ANTIKNOCKBACK);
        types.add(EnumCheatType.REACH);
        types.add(EnumCheatType.DOLPHIN);

        for (EnumCheatType type: types)
            type.setSelected(false);
        return types.toArray(new EnumCheatType[0]);
    }

    public static ArrayList<EnumCheatType> getFullTypeArrayList()
    {
        ArrayList<EnumCheatType> types = new ArrayList<>();

        types.add(EnumCheatType.FLY);
        types.add(EnumCheatType.KILLAURA);
        types.add(EnumCheatType.AUTOCLICKER);
        types.add(EnumCheatType.SPEED);
        types.add(EnumCheatType.ANTIKNOCKBACK);
        types.add(EnumCheatType.REACH);
        types.add(EnumCheatType.DOLPHIN);
        for (EnumCheatType type: types)
            type.setSelected(false);
        return types;
    }
}
