package delegates;

import communitygarden.model.Garden;
import javafx.util.Pair;
import org.jxmapviewer.viewer.GeoPosition;

import java.util.List;

public interface CommunityGardenDelegate {

    List<Garden> getGardens();

    List<Pair<Garden, GeoPosition>> getGardenGeoPositions(List<Garden> gardens);

    void insertGarden(String description);

}
