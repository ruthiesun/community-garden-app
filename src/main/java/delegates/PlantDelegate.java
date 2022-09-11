package delegates;

import plant.model.PlantModel;

public interface PlantDelegate {
    PlantModel[] getPlantModel(String q, boolean annual, boolean perennial, boolean biennial);
}
