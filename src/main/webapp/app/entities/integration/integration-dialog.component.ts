import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { Integration } from './integration.model';
import { IntegrationPopupService } from './integration-popup.service';
import { IntegrationService } from './integration.service';
import { Country, CountryService } from '../country';

@Component({
    selector: 'jhi-integration-dialog',
    templateUrl: './integration-dialog.component.html'
})
export class IntegrationDialogComponent implements OnInit {

    integration: Integration;
    isSaving: boolean;

    countries: Country[];

    constructor(
        public activeModal: NgbActiveModal,
        private dataUtils: JhiDataUtils,
        private jhiAlertService: JhiAlertService,
        private integrationService: IntegrationService,
        private countryService: CountryService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.countryService.query()
            .subscribe((res: HttpResponse<Country[]>) => { this.countries = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.integration.id !== undefined) {
            this.subscribeToSaveResponse(
                this.integrationService.update(this.integration));
        } else {
            this.subscribeToSaveResponse(
                this.integrationService.create(this.integration));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Integration>>) {
        result.subscribe((res: HttpResponse<Integration>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Integration) {
        this.eventManager.broadcast({ name: 'integrationListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackCountryById(index: number, item: Country) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-integration-popup',
    template: ''
})
export class IntegrationPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private integrationPopupService: IntegrationPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.integrationPopupService
                    .open(IntegrationDialogComponent as Component, params['id']);
            } else {
                this.integrationPopupService
                    .open(IntegrationDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
