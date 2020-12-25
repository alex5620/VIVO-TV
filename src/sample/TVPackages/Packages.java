package sample.TVPackages;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Packages {
    private static Packages packages = new Packages();
    private ObservableList<TVPackageData> allPackages;

    private Packages() {}

    void addData(int page, int itemsPerPage, String name) {
        allPackages = FXCollections.observableArrayList(PackagesDatabaseHandler.getInstance().getPackages(page, itemsPerPage, name));
    }

    public static Packages getPackages() {
        return packages;
    }

    public int getSize() {
        return allPackages.size();
    }

    public void addPackage(TVPackageData packageData)
    {
        allPackages.add(packageData);
    }

    public void removePackage(TVPackageData packageData)
    {
        allPackages.remove(packageData);
    }

    public ObservableList<TVPackageData> getAllPackages() {
        return allPackages;
    }
}
