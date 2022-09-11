package delegates;

import plant.model.ThrivesInModel;

public interface ThrivesInDelegate {
    ThrivesInModel[] getThrivesInModelGroupByName(String q, boolean count, boolean min, boolean max);
    ThrivesInModel[] getThrivesInModel(String q);
}
