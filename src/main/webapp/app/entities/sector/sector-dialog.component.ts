import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Sector } from './sector.model';
import { SectorPopupService } from './sector-popup.service';
import { SectorService } from './sector.service';

@Component({
    selector: 'jhi-sector-dialog',
    templateUrl: './sector-dialog.component.html'
})
export class SectorDialogComponent implements OnInit {

    sector: Sector;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private sectorService: SectorService,
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
        if (this.sector.id !== undefined) {
            this.subscribeToSaveResponse(
                this.sectorService.update(this.sector));
        } else {
            this.subscribeToSaveResponse(
                this.sectorService.create(this.sector));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Sector>>) {
        result.subscribe((res: HttpResponse<Sector>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Sector) {
        this.eventManager.broadcast({ name: 'sectorListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-sector-popup',
    template: ''
})
export class SectorPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private sectorPopupService: SectorPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.sectorPopupService
                    .open(SectorDialogComponent as Component, params['id']);
            } else {
                this.sectorPopupService
                    .open(SectorDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
