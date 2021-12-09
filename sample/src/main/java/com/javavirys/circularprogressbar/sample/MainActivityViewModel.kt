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

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainActivityViewModel : ViewModel() {

    val speedLiveData = MutableLiveData<Int>()

    var launchFlag = false

    fun start() {
        if (launchFlag) return
        launchFlag = true
        viewModelScope.launch(Dispatchers.IO) {
            while (launchFlag) {
                speedLiveData.postValue(Random.nextInt(0, 100))
                delay(150)
            }
        }
    }

    fun stop() {
        launchFlag = false
    }
}