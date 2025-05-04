	package com.makara.invoicegenerator.paginator;

    import org.springframework.data.domain.Page;

    import java.util.ArrayList;
    import java.util.List;

		public class PageRender<T> {

			private String url;
			private Page<T> page;

			private int totalPages;
			private int elementsPerPage;
			private int currentPage;

			private List<PageItem> pages;

			public PageRender(String url, Page<T> page) {
				this.url = url;
				this.page = page;
				this.pages = new ArrayList<>();

				elementsPerPage = page.getSize();
				totalPages = page.getTotalPages();
				currentPage = page.getNumber() + 1;

				int from, to;
				if (totalPages <= elementsPerPage) {
					from = 1;
					to = totalPages;
				} else {
					if (currentPage <= elementsPerPage / 2) {
						from = 1;
						to = elementsPerPage;
					} else if (currentPage >= totalPages - elementsPerPage / 2) {
						from = totalPages - elementsPerPage + 1;
						to = elementsPerPage;
					} else {
						from = currentPage - elementsPerPage / 2;
						to = elementsPerPage;
					}
				}

				for (int i = 0; i < to; i++) {
					pages.add(new PageItem(from + i, currentPage == from + i));
				}
			}

			public boolean isFirst() {
				return page.isFirst();
			}

			public boolean isLast() {
				return page.isLast();
			}

			public boolean hasNext() {
				return page.hasNext();
			}

			public boolean hasPrevious() {
				return page.hasPrevious();
			}

			public String getUrl() {
				return url;
			}

			public int getTotalPages() {
				return totalPages;
			}

			public int getCurrentPage() {
				return currentPage;
			}

			public List<PageItem> getPages() {
				return pages;
			}
		}