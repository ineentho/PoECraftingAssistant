/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package craftingbot;

import craftingbot.filtertypes.*;
import java.awt.AWTException;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author charl
 */
public class Filter
{
    public static ArrayList<Filter> FiltersParent = new ArrayList<Filter>();
    
    public ArrayList<FilterBase> filters = new ArrayList<FilterBase>();
    
    public Filter()
    {
//        /*
        Mod ms = new Mod("% increased movement speed", 25, 35);        
        Mod totalES = new Mod("energy shield: ", 130, 1000);
        Mod fRes = new Mod("% to fire resistance", 30, 48);
        Mod lRes = new Mod("% to lightning resistance", 30, 48);
        Mod cRes = new Mod("% to cold resistance", 30, 48);
        filters.add(new Count(6, ms, ms, ms, totalES, totalES, totalES, fRes, fRes, lRes, lRes, cRes, cRes));
//        */
        
//        Mod life = new Mod(" to maximum life", 80, 89);
        
        /* HERALD ABUSE JEWELS
        Mod ph = new Mod(" added passive skill is purposeful harbinger", 1, 2);
        Mod end = new Mod(" added passive skill is endbringer", 1, 1);
        Mod her = new Mod(" added passive skill is heraldry", 1, 1);
        filters.add(new And(ph));
        filters.add(new Count(1, end, her));
        */
        
        /* EXPLODE MOD ON CHEST
        Mod explode = new Mod("enemies you kill explode, dealing 3% of their life as physical damage");
        filters.add(new Count(1, explode));
        */
        
        FiltersParent.add(this);
        
        print();
    }
    
    public static boolean checkIfHit() throws AWTException, UnsupportedFlavorException, IOException
    {
        for (Filter f : FiltersParent)
        {
            String mods = Utility.copy();
            Utility.delay(40);

            mods = mods.toLowerCase();
            
            int goal = f.filters.size();
            int numhit = 0;
            
            for (FilterBase fb : f.filters)
            {
                if (fb.hit(mods)) numhit++;
            }
            
            if (numhit >= goal) return true;
        }
        return false;
    }
    
    public static void print()
    {
        int i = 1;
        for (Filter f : FiltersParent)
        {
            System.out.println(i + ")");
            i++;
            for (FilterBase fb : f.filters)
            {
                fb.print();
            }
        }
    }
}