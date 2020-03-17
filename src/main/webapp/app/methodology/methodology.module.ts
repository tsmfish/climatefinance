import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ClimatefinanceSharedModule } from '../shared';
import { MethodologyComponent, MethodologyEditComponent, MethodologyService, METHODOLOGY_ROUTE, METHODOLOGY_EDIT_ROUTE } from './';
import { CKEditorModule } from '@ckeditor/ckeditor5-angular';
import { ChartService } from 'app/charts/charts.service';

@NgModule({
    imports: [
        ClimatefinanceSharedModule,
        RouterModule.forRoot([METHODOLOGY_ROUTE, METHODOLOGY_EDIT_ROUTE], { useHash: true }),
        CKEditorModule
    ],
    declarations: [MethodologyComponent, MethodologyEditComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
    providers: [MethodologyService]
})
export class ClimatefinanceAppMethodologyModule {}
