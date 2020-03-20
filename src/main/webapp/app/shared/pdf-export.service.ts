import { Injectable } from '@angular/core';

import * as html2canvas from 'html2canvas';
import * as html2pdf from 'html2pdf.js';

import * as pdfMake from 'pdfmake/build/pdfmake';
import * as pdfFonts from 'pdfmake/build/vfs_fonts';

@Injectable()
export class PdfExportService {
    constructor() {
        Object.assign(pdfMake, { vfs: pdfFonts.pdfMake.vfs });
    }

    private canvasExport(el: HTMLElement, options) {
        const exportBtn = el.querySelector('.btn-export-pdf') as any;
        if (exportBtn) {
            exportBtn.hidden = true;
        }
        return html2canvas(el, {
            height: el.clientHeight + 40,
            width: el.clientWidth + 20,
            backgroundColor: null,
            logging: false,
            x: el.getBoundingClientRect().left - 10,
            y: window.scrollY + el.getBoundingClientRect().top,
            onclone: document => {
                if (exportBtn) {
                    exportBtn.hidden = false;
                }
            }
        }).then(canvas => {
            // Get chart data so we can append to the pdf
            const chartData = canvas.toDataURL();
            // Prepare pdf structure
            const docDefinition = Object.assign({}, options.docDefinition || {}, {
                content: [{ image: chartData, width: 500 }],
                pageOrientation: 'landscape',
                defaultStyle: {
                    alignment: 'center'
                }
            });

            pdfMake.createPdf(docDefinition).download('chartToPdf' + '.pdf');
        });
    }
    private htmlExport(el: HTMLElement, options: any) {
        return html2pdf()
            .from(el)
            .save('document.pdf');
    }

    exportPdf(el: HTMLElement, exportType = 'canvas', options = {}): Promise<void> {
        switch (exportType) {
            case 'html':
                return this.htmlExport(el, options);
            case 'canvas':
            default:
                return this.canvasExport(el, options) as Promise<void>;
        }
    }
}
