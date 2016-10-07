/*
##############################################################################
# Copyright (c) 1998-2015,2016 Free Software Foundation, Inc.                #
#                                                                            #
# Permission is hereby granted, free of charge, to any person obtaining a    #
# copy of this software and associated documentation files (the "Software"), #
# to deal in the Software without restriction, including without limitation  #
# the rights to use, copy, modify, merge, publish, distribute, distribute    #
# with modifications, sublicense, and/or sell copies of the Software, and to #
# permit persons to whom the Software is furnished to do so, subject to the  #
# following conditions:                                                      #
#                                                                            #
# The above copyright notice and this permission notice shall be included in #
# all copies or substantial portions of the Software.                        #
#                                                                            #
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR #
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,   #
# FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL    #
# THE ABOVE COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER      #
# LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING    #
# FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER        #
# DEALINGS IN THE SOFTWARE.                                                  #
#                                                                            #
# Except as contained in this notice, the name(s) of the above copyright     #
# holders shall not be used in advertising or otherwise to promote the sale, #
# use or other dealings in this Software without prior written               #
# authorization.                                                             #
##############################################################################
*/
package jmercutio.terminfo;

/**
 * Taken from https://github.com/mirror/ncurses/blob/master/include/Caps
 */

public enum BooleanCapability {
	auto_left_margin("bw", "bw", "YB-G-", "cub1 wraps from column 0 to last column"),
	auto_right_margin("am", "am", "YBCGE", "terminal has automatic margins"),
	no_esc_ctlc("xsb", "xb", "YBCG-", "beehive (f1=escape, f2=ctrl C)"),
	ceol_standout_glitch("xhp", "xs", "YBCGE", "standout not erased by overwriting (hp)"),
	eat_newline_glitch("xenl", "xn", "YBCGE", "newline ignored after 80 cols (concept)"),
	erase_overstrike("eo", "eo", "YBCG-", "can erase overstrikes with a blank"),
	generic_type("gn", "gn", "YB-G-", "generic line type"),
	hard_copy("hc", "hc", "YBCG-", "hardcopy terminal"),
	has_meta_key("km", "km", "YB-GE", "Has a meta key (i.e., sets 8th-bit)"),
	has_status_line("hs", "hs", "YB-G-", "has extra status line"),
	insert_null_glitch("in", "in", "YBCGE", "insert mode distinguishes nulls"),
	memory_above("da", "da", "YBCG-", "display may be retained above the screen"),
	memory_below("db", "db", "YB-GE", "display may be retained below the screen"),
	move_insert_mode("mir", "mi", "YBCGE", "safe to move while in insert mode"),
	move_standout_mode("msgr", "ms", "YBCGE", "safe to move while in standout mode"),
	over_strike("os", "os", "YBCG-", "terminal can overstrike"),
	status_line_esc_ok("eslok", "es", "YB-G-", "escape can be used on the status line"),
	dest_tabs_magic_smso("xt", "xt", "YBCGE", "tabs destructive, magic so char (t1061)"),
	tilde_glitch("hz", "hz", "YB-GE", "cannot print ~'s (Hazeltine)"),
	transparent_underline("ul", "ul", "YBCGE", "underline character overstrikes"),
	xon_xoff("xon", "xo", "YB---", "terminal uses xon/xoff handshaking"),
	needs_xon_xoff("nxon", "nx", "", "padding will not work, xon/xoff required"),
	prtr_silent("mc5i", "5i", "", "printer will not echo on screen"),
	hard_cursor("chts", "HC", "", "cursor is hard to see"),
	non_rev_rmcup("nrrmc", "NR", "", "smcup does not reverse rmcup"),
	no_pad_char("npc", "NP", "", "pad character does not exist"),
	non_dest_scroll_region("ndscr", "ND", "", "scrolling region is non-destructive"),
	can_change("ccc", "cc", "", "terminal can re-define existing colors"),
	back_color_erase("bce", "ut", "", "screen erased with background color"),
	hue_lightness_saturation("hls", "hl", "", "terminal uses only HLS color notation (Tektronix)"),
	col_addr_glitch("xhpa", "YA", "", "only positive motion for hpa/mhpa caps"),
	cr_cancels_micro_mode("crxm", "YB", "", "using cr turns off micro mode"),
	has_print_wheel("daisy", "YC", "", "printer needs operator to change character set"),
	row_addr_glitch("xvpa", "YD", "", "only positive motion for vpa/mvpa caps"),
	semi_auto_right_margin("sam", "YE", "", "printing in last column causes cr"),
	cpi_changes_res("cpix", "YF", "", "changing character pitch changes resolution"),
	lpi_changes_res("lpix", "YG", "", "changing line pitch changes resolution"),

// Screen extensions:
	screen_ax("AX", "AX", "", "Does  understand  ANSI  set  default fg/bg color (\\E[39m \\E[49m)"),
	screen_g0("G0", "G0", "", "Terminal can deal with ISO 2022  font  selection sequences."),
	screen_xt("XT", "XT", "", "Terminal understands special xterm sequences  (OSC, mouse tracking"),
	
// Obsolete:
	backspaces_with_bs("OTbs", "bs", "YBCGE", "uses ^H to move left"),
	crt_no_scrolling("OTns", "ns", "YBCG-", "crt cannot scroll"),
	no_correctly_working_cr("OTnc", "nc", "YBCG", "no way to go to start of line");
	
	String capabilityName;
	String tcapCode;
	String description;
	boolean obsolete;
	
	private BooleanCapability(String capabilityName, String tcapCode, String unused, String description) {
		this.capabilityName = capabilityName;
		this.tcapCode = tcapCode;
		this.description = description;
		this.obsolete = capabilityName.startsWith("OT");
	}

	public static BooleanCapability fromTerminfo(String name) {
		for (BooleanCapability cap: BooleanCapability.values()) {
			if (name.equals(cap.capabilityName)) {
				return cap;
			}
		}
		return null;
	}

	public boolean isObsolete() {
		return obsolete; 
	}
}