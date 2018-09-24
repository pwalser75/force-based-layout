package ch.frostnova.force.based.layout.animation;

import ch.frostnova.force.based.layout.model.Scene;

/**
 * @author pwalser
 * @since 24.09.2018.
 */
public interface LayoutAnimation {

    Scene animate(Scene scene, double elapsedTimeSec, double timeDeltaSec);

}
