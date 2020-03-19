import './vendor.ts';

import { NgModule, Injector } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { Ng2Webstorage, LocalStorageService, SessionStorageService } from 'ngx-webstorage';
import { JhiEventManager } from 'ng-jhipster';

import { AuthInterceptor } from './blocks/interceptor/auth.interceptor';
import { AuthExpiredInterceptor } from './blocks/interceptor/auth-expired.interceptor';
import { ErrorHandlerInterceptor } from './blocks/interceptor/errorhandler.interceptor';
import { NotificationInterceptor } from './blocks/interceptor/notification.interceptor';
import { ClimatefinanceSharedModule } from 'app/shared';
import { ClimatefinanceCoreModule } from 'app/core';
import { ClimatefinanceAppRoutingModule } from './app-routing.module';
import { ClimatefinanceHomeModule } from './home/home.module';
import { ClimatefinanceAccountModule } from './account/account.module';
import { ClimatefinanceEntityModule } from './entities/entity.module';
import { ClimatefinanceAppAboutModule } from './about/about.module';
import { ClimatefinanceAppChartsModule } from './charts/charts.module';
import { ClimatefinanceAppMethodologyModule } from './methodology/methodology.module';
import { ClimatefinanceAppReportsModule } from './reports/reports.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here

import { HttpClientModule, HttpClient } from '@angular/common/http';
import { JhiMainComponent, NavbarComponent, FooterComponent, ProfileService, PageRibbonComponent, ErrorComponent } from './layouts';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

@NgModule({
    imports: [
        BrowserModule,
        BrowserAnimationsModule,
        ClimatefinanceAppRoutingModule,
        Ng2Webstorage.forRoot({ prefix: 'jhi', separator: '-' }),
        ClimatefinanceSharedModule,
        ClimatefinanceCoreModule,
        ClimatefinanceHomeModule,
        ClimatefinanceAccountModule,
        ClimatefinanceEntityModule,
        ClimatefinanceAppAboutModule,
        ClimatefinanceAppChartsModule,
        ClimatefinanceAppMethodologyModule,
        ClimatefinanceAppReportsModule,
        // jhipster-needle-angular-add-module JHipster will add new module here
        HttpClientModule
    ],
    declarations: [JhiMainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent],
    providers: [
        {
            provide: HTTP_INTERCEPTORS,
            useClass: AuthInterceptor,
            multi: true,
            deps: [LocalStorageService, SessionStorageService]
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: AuthExpiredInterceptor,
            multi: true,
            deps: [Injector]
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: ErrorHandlerInterceptor,
            multi: true,
            deps: [JhiEventManager]
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: NotificationInterceptor,
            multi: true,
            deps: [Injector]
        }
    ],
    bootstrap: [JhiMainComponent]
})
export class ClimatefinanceAppModule {}
