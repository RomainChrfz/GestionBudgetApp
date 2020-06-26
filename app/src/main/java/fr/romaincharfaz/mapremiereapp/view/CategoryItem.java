package fr.romaincharfaz.mapremiereapp.view;

public class CategoryItem {
    private String mCategoryName;
    private int mFlagIcon;

    public CategoryItem(String CategoryName, int FlagIcon) {
        mCategoryName = CategoryName;
        mFlagIcon = FlagIcon;
    }

    public String getCategoryName() { return mCategoryName; }
    public int getFlagIcon() { return mFlagIcon; }

}
