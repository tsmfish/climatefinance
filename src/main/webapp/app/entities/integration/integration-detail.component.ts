import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';

import { Integration } from './integration.model';
import { IntegrationService } from './integration.service';

@Component({
    selector: 'jhi-integration-detail',
    templateUrl: './integration-detail.component.html'
})
export class IntegrationDetailComponent implements OnInit, OnDestroy {

    integration: Integration;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private dataUtils: JhiDataUtils,
        private integrationService: IntegrationService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInIntegrations();
    }

    load(id) {
        this.integrationService.find(id)
            .subscribe((integrationResponse: HttpResponse<Integration>) => {
                this.integration = integrationResponse.body;
            });
    }
    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInIntegrations() {
        this.eventSubscriber = this.eventManager.subscribe(
            'integrationListModification',
            (response) => this.load(this.integration.id)
        );
    }
}
