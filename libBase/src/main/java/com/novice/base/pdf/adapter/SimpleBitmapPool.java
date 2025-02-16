/*
 * Copyright (C) 2016 Olmo Gallegos Hernández.
 * Copyright (C) 2016 Franc Krueger.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.novice.base.pdf.adapter;

import android.graphics.Bitmap;
import android.graphics.Color;

public class SimpleBitmapPool {

    Bitmap[] bitmaps;

    private int poolSize;

    private int width;

    private int height;

    private Bitmap.Config config;

    public SimpleBitmapPool(PdfRendererParams params) {
        this.poolSize = getPoolSize(params.getOffScreenSize());
        this.width = params.getWidth();
        this.height = params.getHeight();
        this.config = params.getConfig();
        this.bitmaps = new Bitmap[poolSize];
    }

    private int getPoolSize(int offScreenSize) {
        return (offScreenSize) * 2 + 1;
    }

    public Bitmap getBitmap(int position) {
        int index = getIndexFromPosition(position);
        if (bitmaps[index] == null) {
            createBitmapAtIndex(index);
        }

        bitmaps[index].eraseColor(Color.TRANSPARENT);

        return bitmaps[index];
    }

    public Bitmap get(int position) {
        return getBitmap(position);
    }

    public void clear() {
        recycleAll();
    }

    protected void recycleAll() {
        for (int i = 0; i < poolSize; i++) {
            if (bitmaps[i] != null) {
                bitmaps[i].recycle();
                bitmaps[i] = null;
            }
        }
    }

    protected void createBitmapAtIndex(int index) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);
        bitmaps[index] = bitmap;
    }

    protected int getIndexFromPosition(int position) {
        return position % poolSize;
    }
}