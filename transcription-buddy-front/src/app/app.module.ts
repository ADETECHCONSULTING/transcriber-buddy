import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { DragDropComponent } from './drag-drop/drag-drop.component';
import { DndDirective } from './dnd.directive';
import { ProgressComponent } from './progress/progress/progress.component';

@NgModule({
  declarations: [
    AppComponent,
    DragDropComponent,
    DndDirective,
    ProgressComponent
  ],
  imports: [
    BrowserModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
