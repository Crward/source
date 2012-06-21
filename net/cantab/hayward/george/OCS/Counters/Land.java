/*
 *
 * Copyright (c) 2010 by George Hayward
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License (LGPL) as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, copies are available
 * at http://www.opensource.org.
 */

package net.cantab.hayward.george.OCS.Counters;

import VASSAL.build.module.gamepieceimage.StringEnumConfigurer;
import VASSAL.configure.StringConfigurer;
import VASSAL.counters.GamePiece;
import VASSAL.counters.PieceEditor;
import VASSAL.tools.SequenceEncoder;
import java.awt.Component;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import net.cantab.hayward.george.OCS.Statics;

/**
 * Land Units
 * @author George Hayward
 */
public abstract class Land extends Unit {

    /**
     * Constructor
     */
    Land(String type, GamePiece p) {
        super(type, p);
        SequenceEncoder.Decoder st = new SequenceEncoder.Decoder(type, ';');
        st.nextToken();
        st.nextToken();
        factors = st.nextToken("");
        division = st.nextToken("");
    }

    /** The type information is information that does not change
     * during the course of a game.  All this information has been moved into
     * the @see PieceData class so it is just necessary to save which one this
     * piece uses.
     * @see BasicCommandEncoder */
    @Override
    public String myGetType() {
        SequenceEncoder se = new SequenceEncoder(';');
        se.append(factors);
        se.append(division);
        return super.myGetType() + ";" +  se.getValue();
    }

    /*
     * The methods needed by the EditablePiece interface
     */
     /** A plain-English description of this type of piece */

    /** Set the type information for this piece.  See {@link Decorator#myGetType} */
    @Override
    public void mySetType(String type) {
        super.mySetType(type);
        SequenceEncoder.Decoder st = new SequenceEncoder.Decoder(type, ';');
        st.nextToken();
        st.nextToken();
        factors = st.nextToken("");
        division = st.nextToken("");
    }

    /** Get the configurer for this trait */
    @Override
    public PieceEditor getEditor() {
        return new Ed2();
    }

    public class Ed2 implements PieceEditor {

        private StringEnumConfigurer sideConfig;
        private StringConfigurer factorsConfig;
        private StringConfigurer divConfig;
        private int aSide;
        private JPanel panel;

        public Ed2() {
            sideConfig = new StringEnumConfigurer(null,
                    "Pick the side",
                    new String[]{Statics.theSides[0].name,
                        Statics.theSides[1].name});
            if ( theSide != -1 )
                sideConfig.setValue(Statics.theSides[theSide].name);
            factorsConfig = new StringConfigurer(null, "Factors: ", factors);
            divConfig = new StringConfigurer(null, "Division: ", division);

            panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

            panel.add(sideConfig.getControls());
            panel.add(factorsConfig.getControls());
            panel.add(divConfig.getControls());

        }

        public Component getControls() {
            return panel;
        }

        public String getType() {
            aSide = -1;
            String x = sideConfig.getValueString();
            if (x.equals(Statics.theSides[0].name)) {
                aSide = 0;
            }
            if (x.equals(Statics.theSides[1].name)) {
                aSide = 1;
            }
            SequenceEncoder se = new SequenceEncoder(';');
            se.append(aSide);
            se.append(factorsConfig.getValueString());
            se.append(divConfig.getValueString());
            return myID() + se.getValue();
        }

        public String getState() {
            return "";
        }
    }
}
