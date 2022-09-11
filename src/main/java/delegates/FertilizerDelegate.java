package delegates;

import plant.model.FertilizerModel;

public interface FertilizerDelegate {
    FertilizerModel[] getFertilizerModel(String q, boolean npk, boolean type, boolean details);
}
