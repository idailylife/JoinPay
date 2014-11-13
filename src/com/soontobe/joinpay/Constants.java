/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.soontobe.joinpay;

import java.util.Map;

import org.json.JSONObject;

public class Constants {
	public static String DemoMyName = "Lirong";
	
	public static String[] DemoUserNameList = {
		"Luna",
		"Bowei",
		"Jone",
		"Doe",
		"Whoever",
		""
	};

	static String userName;
	static String folderName = "lirong2";
	static String urlPrefix = "http://www.posttestserver.com/data/2014/11/12/" + folderName;
	static String urlForCreatingFolder = "https://posttestserver.com/post.php?dir=" + folderName;

	static String[][] macAddressToName = {
		{ "a0:0b:ba:e7:05:b5", "Lirong"},
		{ "10:68:3f:fc:0e:d9", "Luna"},
		{ "10:68:3f:43:5c:35", "Bowei"},
		{ "asdfkjklasdjgklajsdlfjalsdjfalsdj", "Benny"},
	};

    static String[] contactNameList = {
    	"Alice",
        "Amy",
        "Benny",
        "Bob",
        "Catherine",
        "Frank",
        "Jack",
        "Jason",
        "Kate",
        "Melissa",
        "Patrick",
        "Teddy",
        "Tommy",
        "Yu"
    };
    
    static String[] deviceNameList = {
    	"Lirong",
    	"Luna",
    	"Bowei",
    	"Benny"
    };
    
    // Alphabetical
    static String[][] NameList2 = {
    	{"Alice, Amy"}, 
    	{"Benny", "Bob"}, 
    	{"Catherine"}, 
    	{}, 
    	{}, 
    	{"Frank"}, 
    	{}, 
    	{}, 
    	{}, 
    	{"Jack", "Jason"}, 
    	{"Kate"}, 
    	{}, 
    	{"Melissa"}, 
    	{"Nick"}, 
    	{}, 
    	{"Patrick"}, 
    	{}, 
    	{}, 
    	{}, 
    	{"Teddy", "Tommo"}, 
    	{}, 
    	{}, 
    	{}, 
    	{}, 
    	{}, 
    	{}
    };
}
