package com.anna.lozytska.achievementstimer.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anna.lozytska.achievementstimer.R;

/**
 * Created by Anna Lozytska on 01.09.16.
 */
public class CommentsFragment extends Fragment {
    private static final String PARENT_TASK_ID = "parent_task_id";

    public static CommentsFragment newInstance(long parentTaskId) {
        Bundle args = new Bundle();
        args.putLong(PARENT_TASK_ID, parentTaskId);
        CommentsFragment fragment = new CommentsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_comments, container, false);

        return root;
    }
}
