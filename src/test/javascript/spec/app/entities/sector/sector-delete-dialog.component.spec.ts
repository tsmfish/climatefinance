/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { ClimatefinanceTestModule } from '../../../test.module';
import { SectorDeleteDialogComponent } from 'app/entities/sector/sector-delete-dialog.component';
import { SectorService } from 'app/entities/sector/sector.service';

describe('Component Tests', () => {
    describe('Sector Management Delete Component', () => {
        let comp: SectorDeleteDialogComponent;
        let fixture: ComponentFixture<SectorDeleteDialogComponent>;
        let service: SectorService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ClimatefinanceTestModule],
                declarations: [SectorDeleteDialogComponent]
            })
                .overrideTemplate(SectorDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SectorDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SectorService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it(
                'Should call delete service on confirmDelete',
                inject(
                    [],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(of({}));

                        // WHEN
                        comp.confirmDelete(123);
                        tick();

                        // THEN
                        expect(service.delete).toHaveBeenCalledWith(123);
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });
});
