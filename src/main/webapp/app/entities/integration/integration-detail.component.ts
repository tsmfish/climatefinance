import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IIntegration } from 'app/shared/model/integration.model';

@Component({
    selector: 'jhi-integration-detail',
    templateUrl: './integration-detail.component.html'
})
export class IntegrationDetailComponent implements OnInit {
    integration: IIntegration;

    constructor(private dataUtils: JhiDataUtils, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ integration }) => {
            this.integration = integration;
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
}
