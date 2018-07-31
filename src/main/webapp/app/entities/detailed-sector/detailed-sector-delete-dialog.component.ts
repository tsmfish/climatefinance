import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { DetailedSector } from './detailed-sector.model';
import { DetailedSectorPopupService } from './detailed-sector-popup.service';
import { DetailedSectorService } from './detailed-sector.service';

@Component({
    selector: 'jhi-detailed-sector-delete-dialog',
    templateUrl: './detailed-sector-delete-dialog.component.html'
})
export class DetailedSectorDeleteDialogComponent {

    detailedSector: DetailedSector;

    constructor(
        private detailedSectorService: DetailedSectorService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.detailedSectorService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'detailedSectorListModification',
                content: 'Deleted an detailedSector'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-detailed-sector-delete-popup',
    template: ''
})
export class DetailedSectorDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private detailedSectorPopupService: DetailedSectorPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.detailedSectorPopupService
                .open(DetailedSectorDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
