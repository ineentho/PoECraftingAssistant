package craftingbot.UI;

import craftingbot.Main;
import craftingbot.Modifier;
import craftingbot.filtertypes.FilterBase;
import craftingbot.filtertypes.Mod;
import craftingbot.filtertypes.Id;
import craftingbot.filtertypes.logicgroups.Count;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;
import java.io.File;
import java.util.ArrayList;

public class ModifierPanel extends JPanel {
     
    public String resourcePath;
    public Main frame;
    public FilterBase filterbase;
    public Mod mod;
    public FilterTypePanel parent;
    
    public MPMinMax min;
    public MPMinMax max;
    
    
    public ModifierPanel(Main frame, FilterTypePanel parent, FilterBase filterbase, Mod mod)
    {
        String path = "src/main/resources";
        File file = new File(path);
        path = file.getAbsolutePath();
        this.resourcePath = path;
        this.frame = frame;
        this.filterbase = filterbase;
        this.mod = mod;
        this.parent = parent;
        
        if (mod == null)
        {
            mod = new Mod("New Modifier");
            filterbase.mods.add(mod);
            min = new MPMinMax(this, "min", true);
            max = new MPMinMax(this, "max", false);
        }
        
        Dimension size = new Dimension((int) (parent.getWidth() * 0.912),(int) (40)); // 0.912
        setSize(size);
        setPreferredSize(size);
        setBackground(new Color(60,60,60));
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        CloseMPButton cb = new CloseMPButton(this);
        ModLabel ml = new ModLabel(this, mod.name);
        
        min = new MPMinMax(this, String.valueOf(mod.ID.min), true);
        max = new MPMinMax(this, String.valueOf(mod.ID.max), false);
        
        add(cb, Box.LEFT_ALIGNMENT);
        add(Box.createRigidArea(new Dimension(15,0)), Box.LEFT_ALIGNMENT);
        add(ml, Box.LEFT_ALIGNMENT);
        
        add(Box.createHorizontalGlue());
        
        add(min, Box.RIGHT_ALIGNMENT);
        add(max, Box.RIGHT_ALIGNMENT);
        
        addMouseListener(new ModMouseListener(this));
        
        parent.add(this);
    }
    
    public void showSearchBox()
    {
        String[] types = SearchBox.toArr(Modifier.all);
        SearchBox sb = new SearchBox(types);
        
        JOptionPane jop = new JOptionPane();
        sb.setSelectedIndex(0);        

        jop.showMessageDialog(this, sb, "CraftingBot", JOptionPane.PLAIN_MESSAGE, null);
        
        String selected = sb.getSelectedItem().toString();
        
        if (selected != null && !selected.equals(""))
        {
            Modifier m = Modifier.getFromStr(selected);
            m.print();
        }
    }
}

class ModMouseListener implements MouseListener {
    
    ModifierPanel owner;
    
    public ModMouseListener(ModifierPanel owner)
    {
        this.owner = owner;
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        owner.showSearchBox();
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}

class CloseMPButton extends JButton {
    public CloseMPButton(ModifierPanel parent)
    {
        setBorderPainted(false);
        setFocusPainted(false);
        setContentAreaFilled(true);
        setOpaque(true);
        setPreferredSize(new Dimension((int) (parent.getWidth() * 0.06),(int) ((32))));
        setBackground(new Color(0,0,0));
        setIcon(new javax.swing.ImageIcon(parent.resourcePath + "/xbuttontransparentsmall.png")); // NOI18N
        setToolTipText("Remove this logic filter");
        addMouseListener(new BackgroundListener(this, new Color(80,80,80), new Color(0,0,0)));
        
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                parent.filterbase.print();
//                parent.parent.modifierpanels.remove(this);
                parent.filterbase.mods.remove(parent.mod);
                FilterTypePanel.reshow();
                parent.filterbase.print();
            }
        };
        addActionListener(actionListener);
    }
}

class ModLabel extends JLabel {
    public ModLabel(ModifierPanel parent, String text)
    {
        setText(text);
        setFont(parent.frame.getNewFont(14));
        setBackground(new Color(255,0,0));
        setForeground(new Color(255,255,255));
        setPreferredSize(new Dimension((int) (parent.getWidth() * 0.74),(int) ((32))));
    }
}

class MPMinMax extends JTextField {
    public String placeholder;
    public boolean isMin; // true = min, false = max;
    public ModifierPanel parent;
    
    public MPMinMax(ModifierPanel parent, String placeholder, boolean isMin)
    {
        if (placeholder.equals("-100000"))
        {
            if (isMin) placeholder = "min";
            else placeholder = "max";
        }
        
        this.placeholder = placeholder;
        this.isMin = isMin;
        this.parent = parent;
        
        setText(placeholder);
        
        setFont(parent.frame.getNewFont(14));
        setBackground(new Color(0,0,0));
        setForeground(new Color(120,120,120));
        
        if (!placeholder.equals("min") && !placeholder.equals("max") && !placeholder.equals("-100000"))
            setForeground(new Color(255,255,255));
        
        setHorizontalAlignment(SwingConstants.CENTER);
        
        addFocusListener(new FListener(this, this.placeholder));
                
        addKeyListener(new KeyListener()
        {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == 10)
                {
                    parent.requestFocusInWindow();
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }
        });
    }
    
    public void focusGained()
    {
        if (getText().equals(placeholder)) {
            setText("");
            setForeground(new Color(255,255,255));
        }
    }
    
    public void focusLost()
    {
        if (getText().isEmpty()) {
            setText(placeholder);
            setForeground(new Color(120,120,120));
        }

        // save value

        if (isMin) parent.mod.ID.min = Integer.valueOf(parent.min.getText());
        else       parent.mod.ID.max = Integer.valueOf(parent.max.getText());
    }
}

class FListener implements FocusListener
{
    String placeholder;
    MPMinMax owner;
    
    public FListener(MPMinMax owner, String placeholder)
    {
        this.placeholder = placeholder;
        this.owner = owner;
    }
    
    @Override
    public void focusGained(FocusEvent e) {
        owner.focusGained();
    }
    @Override
    public void focusLost(FocusEvent e) {
        owner.focusLost();
    }
}