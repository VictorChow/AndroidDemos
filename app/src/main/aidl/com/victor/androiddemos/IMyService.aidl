package com.victor.androiddemos;

import com.victor.androiddemos.IMyActivity;

interface IMyService {
   int getNumber();
   void registerActivity(IMyActivity iActivity);
}
