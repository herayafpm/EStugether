package com.kelompok11.e_stugether.ui.main;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.kelompok11.e_stugether.KelasActivity;
import com.kelompok11.e_stugether.R;
import com.kelompok11.e_stugether.fragment.AnggotaFragment;
import com.kelompok11.e_stugether.fragment.MateriFragment;
import com.kelompok11.e_stugether.fragment.PelajaranFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.Materi, R.string.Pelajaran,R.string.Anggota};
    private final Context mContext;
    private String kelasKey;

    public SectionsPagerAdapter(String kelasKey, Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
        this.kelasKey = kelasKey;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        switch (position) {
            case 0:
                MateriFragment materiFragment = new MateriFragment();
                materiFragment.setKelasKey(kelasKey);
                return materiFragment;
            case 1:
                PelajaranFragment pelajaranFragment = new PelajaranFragment();
                pelajaranFragment.setKelasKey(kelasKey);
                return pelajaranFragment;
            case 2:
                AnggotaFragment anggotaFragment = new AnggotaFragment();
                anggotaFragment.setKelasKey(kelasKey);
                return anggotaFragment;
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return TAB_TITLES.length;
    }
}