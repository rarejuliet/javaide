/*
 *  Copyright (c) 2017 Tran Le Duy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.duy.ide.editor.view.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.duy.ide.R;
import com.duy.ide.autocomplete.model.Description;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Duy on 26-Apr-17.
 */
public class CodeSuggestAdapter extends ArrayAdapter<Description> {
    private static final String TAG = "CodeSuggestAdapter";
    private final Context context;
    private final int colorKeyWord;
    private final int colorNormal;
    private final int colorVariable = 0xffFFB74D;
    private LayoutInflater inflater;
    private ArrayList<Description> clone;
    private ArrayList<Description> suggestion;
    private int resourceID;
    private Filter codeFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            if (resultValue == null) {
                return "";
            }
            return ((Description) resultValue).getName();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            suggestion.clear();
            if (constraint != null) {
                for (Description item : clone) {
//                    if (item.compareTo(constraint.toString()) == 0) {
                        suggestion.add(item);
//                    }
                }
                filterResults.values = suggestion;
                filterResults.count = suggestion.size();
            }
            return filterResults;
        }

        @Override
        @SuppressWarnings("unchecked")

        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<Description> filteredList = (ArrayList<Description>) results.values;
            clear();
            if (filteredList != null && filteredList.size() > 0) {
                addAll(filteredList);
            }
            notifyDataSetChanged();
        }
    };

    @SuppressWarnings("unchecked")
    public CodeSuggestAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<Description> objects) {
        super(context, resource, objects);
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.clone = (ArrayList<Description>) objects.clone();
        this.suggestion = new ArrayList<>();
        this.resourceID = resource;
        colorKeyWord = context.getResources().getColor(R.color.color_key_word_color);
        colorNormal = context.getResources().getColor(android.R.color.primary_text_dark);
    }

    public ArrayList<Description> getAllItems() {
        return clone;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(resourceID, null);
        }

        final Description item = getItem(position);
        TextView txtName = convertView.findViewById(R.id.txt_name);
        txtName.setTypeface(Typeface.MONOSPACE);
        TextView txtType = convertView.findViewById(R.id.txt_type);
        if (item != null) {
            txtName.setText(item.getName());
            txtType.setText(item.getType() != null ? item.getType().getName() : "");
        }
        return convertView;
    }


    public void clearAllData() {
        super.clear();
        clone.clear();
    }

    public void addData(@NonNull Collection<? extends Description> collection) {
        addAll(collection);
        clone.addAll(collection);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return codeFilter;
    }


}
