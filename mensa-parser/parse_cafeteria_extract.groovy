import de.ifgi.lodum.*
import de.ifgi.lodum.parser.*

// Aasee
 turtle = MensaEtlBridge.getTurtle(CafeteriaTyp.Aasee)

 // Write n3 file
 new File("scripts/turtle/mensa_aasee.n3").write(turtle)


// Ring
 turtle = MensaEtlBridge.getTurtle(CafeteriaTyp.Ring)

 // Write n3 file
 new File("scripts/turtle/mensa_ring.n3").write(turtle)


// DaVinci
 turtle = MensaEtlBridge.getTurtle(CafeteriaTyp.DaVinci)

 // Write n3 file
 new File("scripts/turtle/mensa_davinci.n3").write(turtle)


// Bispinghof
 turtle = MensaEtlBridge.getTurtle(CafeteriaTyp.Bispinghof)

 // Write n3 file
 new File("scripts/turtle/mensa_bispinghof.n3").write(turtle)


// Oeconomicum
 turtle = MensaEtlBridge.getTurtle(CafeteriaTyp.Oeconomicum)

 // Write n3 file
 new File("scripts/turtle/mensa_oeconomicum.n3").write(turtle)
