import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { DetailedSector } from './detailed-sector.model';
import { DetailedSectorPopupService } from './detailed-sector-popup.service';
import { DetailedSectorService } from './detailed-sector.service';

@Component({
    selector: 'jhi-detailed-sector-dialog',
    templateUrl: './detailed-sector-dialog.component.html'
})
export class DetailedSectorDialogComponent implements OnInit {

    detailedSector: DetailedSector;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private detailedSectorService: DetailedSectorService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.detailedSector.id !== undefined) {
            this.subscribeToSaveResponse(
                this.detailedSectorService.update(this.detailedSector));
        } else {
            this.subscribeToSaveResponse(
                this.detailedSectorService.create(this.detailedSector));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<DetailedSector>>) {
        result.subscribe((res: HttpResponse<DetailedSector>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: DetailedSector) {
        this.eventManager.broadcast({ name: 'detailedSectorListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-detailed-sector-popup',
    template: ''
})
export class DetailedSectorPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private detailedSectorPopupService: DetailedSectorPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.detailedSectorPopupService
                    .open(DetailedSectorDialogComponent as Component, params['id']);
            } else {
                this.detailedSectorPopupService
                    .open(DetailedSectorDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
