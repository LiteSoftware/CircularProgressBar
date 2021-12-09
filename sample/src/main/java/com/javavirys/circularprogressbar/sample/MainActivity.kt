/*
 * Copyright 2021 Vitaliy Sychov. All rights reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package com.javavirys.circularprogressbar.sample

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.javavirys.circularprogressbar.CircularProgressBar

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val viewModel: MainActivityViewModel by viewModels { MainActivityViewModelFactory() }

    private var speedTextView: TextView? = null

    private var progress: CircularProgressBar? = null

    private var stopButton: Button? = null

    private var startButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initWidgets()
        initLiveData()
    }

    private fun initWidgets() {
        speedTextView = findViewById(R.id.speedTextView)
        progress = findViewById(R.id.progress)

        startButton = findViewById(R.id.startButton)
        startButton?.setOnClickListener {
            viewModel.start()
        }

        stopButton = findViewById(R.id.stopButton)
        stopButton?.setOnClickListener {
            viewModel.stop()
        }
    }

    private fun initLiveData() {
        viewModel.speedLiveData.observe(this) {
            speedTextView?.text = getString(R.string.speed, it)
            progress?.setProgress(it)
        }
    }
}