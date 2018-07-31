import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { IIntegration } from 'app/shared/model/integration.model';
import { Principal } from 'app/core';
import { IntegrationService } from './integration.service';

@Component({
    selector: 'jhi-integration',
    templateUrl: './integration.component.html'
})
export class IntegrationComponent implements OnInit, OnDestroy {
    integrations: IIntegration[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private integrationService: IntegrationService,
        private jhiAlertService: JhiAlertService,
        private dataUtils: JhiDataUtils,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.currentSearch =
            this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search']
                ? this.activatedRoute.snapshot.params['search']
                : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.integrationService
                .search({
                    query: this.currentSearch
                })
                .subscribe(
                    (res: HttpResponse<IIntegration[]>) => (this.integrations = res.body),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
        }
        this.integrationService.query().subscribe(
            (res: HttpResponse<IIntegration[]>) => {
                this.integrations = res.body;
                this.currentSearch = '';
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.currentSearch = query;
        this.loadAll();
    }

    clear() {
        this.currentSearch = '';
        this.loadAll();
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInIntegrations();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IIntegration) {
        return item.id;
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    registerChangeInIntegrations() {
        this.eventSubscriber = this.eventManager.subscribe('integrationListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
