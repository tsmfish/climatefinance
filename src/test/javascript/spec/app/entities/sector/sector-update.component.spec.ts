/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { ClimatefinanceTestModule } from '../../../test.module';
import { SectorUpdateComponent } from 'app/entities/sector/sector-update.component';
import { SectorService } from 'app/entities/sector/sector.service';
import { Sector } from 'app/shared/model/sector.model';

describe('Component Tests', () => {
    describe('Sector Management Update Component', () => {
        let comp: SectorUpdateComponent;
        let fixture: ComponentFixture<SectorUpdateComponent>;
        let service: SectorService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ClimatefinanceTestModule],
                declarations: [SectorUpdateComponent]
            })
                .overrideTemplate(SectorUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(SectorUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SectorService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Sector(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.sector = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Sector();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.sector = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
